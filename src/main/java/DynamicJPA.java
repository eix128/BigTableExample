import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.Pair;
import jpa.CityEntity;
import main.Item;
import main.PairValueCell;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by Kadir on 2/23/2016.
 */
public class DynamicJPA implements ObservableList {

    private ArrayList<ListChangeListener> changeListener;

    private EntityManager entityManager;
    private LoadingCache<Integer, CityEntity> cache;
    private Supplier<Long> cacheSize;


    public DynamicJPA(EntityManager entityManager) {
        changeListener = new ArrayList<ListChangeListener>();
        this.entityManager = entityManager;
//        entityManager.createNamedQuery("select.between", CityEntity.class);
        javax.persistence.criteria.CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery<CityEntity> cq = cb.createQuery(CityEntity.class);


        final Root<CityEntity> rootEntry = cq.from(CityEntity.class);

//        Expression<String> postgresqlCastFunction = cb.function("CAST", String.class, from.<String>get("ID").as(String.class));
//        Predicate<String> p = cb.like(postgresqlCastFunction, "10%");

        final Path<String> city = rootEntry.get("city");
        final Order cityName = cb.asc( city );

//        Expression<String> path = root.get("lastName");
//        Expression<String> upper =critBuilder.upper(path);
//        Predicate ctfPredicate = critBuilder.like(upper,"%stringToFind%")
        final Expression<String> upper = cb.upper(city);
        CriteriaQuery<CityEntity> all = cq.select(rootEntry);
        TypedQuery<CityEntity> allQuery = entityManager.createQuery(all);


//        BigInteger bigInteger = new BigInteger("2323423423423423424234234422343323443342");
//Creating the BloomFilter
//        BloomFilter bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(), 1000);

//Putting elements into the filter
//A BigInteger representing a key of some sort
//        bloomFilter.put(bigInteger.toByteArray());

//Testing for element in set
//        boolean mayBeContained = bloomFilter.mightContain(bigInteger.toByteArray());

//        final List<String> strings = Arrays.asList("", "");
//        Map<String, String > result = strings.stream().collect(Collectors.toMap(String::toString,Function.identity()));

        cache = CacheBuilder.newBuilder()
                .maximumSize(128 * 128 * 32) // maximum 100 records can be cached
//                .maximumSize(256) // maximum 100 records can be cached
                .concurrencyLevel(4)
//                .softValues()
                .expireAfterAccess(10, TimeUnit.SECONDS) // cache will expire after 30 minutes of access
                .build(new CacheLoader<Integer, CityEntity>() {
                    @Override
                    public CityEntity load(Integer tableIndex) throws Exception {
                        final TypedQuery<CityEntity> cityEntityTypedQuery = allQuery.setFirstResult(Math.max(0, tableIndex - 128)).setMaxResults(128);
//                        final String queryString = allQuery.unwrap(Query.class).getQueryString();
                        final List<CityEntity> resultList =  cityEntityTypedQuery.getResultList();
                        CityEntity[] cityEntityPre = new CityEntity[1];
                        final ConcurrentMap<Integer, CityEntity> integerCityEntityConcurrentMap = cache.asMap();
                        final boolean containsKey = integerCityEntityConcurrentMap.containsKey(tableIndex);
                        if (containsKey) {
                            cityEntityPre[0] = cache.get(tableIndex);
                        }
                        final CityEntity[] finalCityEntityPre = cityEntityPre;
                        resultList.parallelStream().forEach(cityEntity -> {
                                    cache.put((int) cityEntity.getCityId(), cityEntity);
                                    if (cityEntity.getCityId() == tableIndex) {
                                        finalCityEntityPre[0] = cityEntity;
                                    }
                                }
                        );
                        //System.out.println(tableIndex);
                        if (!containsKey) {
                            cityEntityPre[0] = finalCityEntityPre[0];
                        }

                        if (cityEntityPre[0] == null) {
                            cityEntityPre[0] = new CityEntity();
                            cityEntityPre[0].setCity("YOK");
                            cityEntityPre[0].setCityId((short) -1);
                        }
                        return cityEntityPre[0];
                    } // build the cacheloader
                });



        CriteriaQuery<Long> countCriteria = cb.createQuery(Long.class);
        Root<?> entityRoot = rootEntry;
        countCriteria.select(cb.count(countCriteria.from(CityEntity.class)));
//        countCriteria.where(countCriteria.getRestriction());
        final TypedQuery<Long> query = entityManager.createQuery(countCriteria);
        cacheSize = new Supplier<Long>() {
            @Override
            public Long get() {
                return query.getSingleResult();
            }
        };
        Suppliers.memoizeWithExpiration(cacheSize, 5, TimeUnit.SECONDS);
    }


    @Override
    public void addListener(ListChangeListener listener) {
        changeListener.add(listener);
    }

    @Override
    public void removeListener(ListChangeListener listener) {
        changeListener.remove(listener);
    }

    @Override
    public boolean addAll(Object[] elements) {
        return false;
    }

    @Override
    public boolean setAll(Object[] elements) {
        return false;
    }

    @Override
    public boolean setAll(Collection col) {
        return false;
    }

    @Override
    public boolean removeAll(Object[] elements) {
        return false;
    }

    @Override
    public boolean retainAll(Object[] elements) {
        return false;
    }

    @Override
    public void remove(int from, int to) {

    }

    @Override
    public FilteredList filtered(Predicate predicate) {
        return null;
    }

    @Override
    public SortedList sorted(Comparator comparator) {
        return null;
    }

    @Override
    public SortedList sorted() {
        return null;
    }

    @Override
    public int size() {
        return cacheSize.get().intValue();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer action) {

    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeIf(Predicate filter) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator operator) {

    }

    @Override
    public void sort(Comparator c) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public Object get(int index) {
        try {
            final String city = cache.get(index).getCity();
//            System.out.println(city);
            return new Item(city, city);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Spliterator spliterator() {
        return null;
    }

    @Override
    public Stream stream() {
        return null;
    }

    @Override
    public Stream parallelStream() {
        return null;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }
}
