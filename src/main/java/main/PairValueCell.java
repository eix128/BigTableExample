package main;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

/**
 * Created by kadir.basol on 7.3.2016.
 */
public class PairValueCell extends TableCell<Item, Object> {
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        final TableColumn<Item, Object> tableColumn = getTableColumn();

        if (item != null) {
            if (item instanceof Item) {
                setText((String)((Item) item).getFirstName() );
                setGraphic(null);
            } else if (item instanceof Integer) {
                setText(Integer.toString((Integer) item));
                setGraphic(null);
            } else if (item instanceof Boolean) {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected((boolean) item);
                setGraphic(checkBox);
            } else if (item instanceof Image) {
                setText(null);
                ImageView imageView = new ImageView((Image) item);
                imageView.setFitWidth(100);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                setGraphic(imageView);
            } else {
                setText(item.toString());
                setGraphic(null);
            }
        } else {
            setText(null);
            setGraphic(null);
        }
    }
    //    @Override
//    protected void updateItem(Item item, boolean empty) {
//        super.updateItem(item, empty);
//

//    }
}