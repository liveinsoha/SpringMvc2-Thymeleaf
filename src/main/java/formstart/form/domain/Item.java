package formstart.form.domain;


import formstart.form.domain.property.ItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter //@Data를 도메인 객체에 직접 쓰는 것은 위험하다. 데이터를 단순 정소아는 데 쓰이는 DTO에는 써도 괜찮은데,,
public class Item {

    private Long id;
    private String name;
    private Integer price; //Integer를 쓰는 이유는 null이라도 들어갈 수 있도록 하기 위함이다. int 타입은 null이 들어가지 못한다,
    private Integer quantity;
    //null일 경우 예외처리 생각할 것

    private Boolean open;
    private List<String> selectedRegions;
    private ItemType itemType;
    private String deliveryCode;

    public Item(String name, Integer price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Item updateItem(String name, Integer price, Integer quantity, Boolean open, List<String> selectedRegions,
                           ItemType itemType, String deliveryCode) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.open = open;
        this.selectedRegions = selectedRegions;
        this.itemType = itemType;
        this.deliveryCode = deliveryCode;
        return this;
    }
}
