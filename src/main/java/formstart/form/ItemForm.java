package formstart.form;

import formstart.form.domain.property.ItemType;
import lombok.Data;

import java.util.List;

@Data
public class ItemForm {

    private String name;
    private Integer price;
    private Integer quantity;

    private Boolean open;
    private List<String> selectedRegions;
    private ItemType itemType;
    private String deliveryCode;

}
