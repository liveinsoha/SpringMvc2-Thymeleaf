package formstart.form.domain;

import formstart.form.ItemForm;
import formstart.form.domain.property.DeliveryCode;
import formstart.form.domain.property.ItemType;
import formstart.form.domain.property.Properties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/basic/items")//공통경로는 여기있다
public class ItemController {

    /**
     * public BasicItemController(ItemRepository itemRepository) {
     * this.itemRepository = itemRepository;
     * 이렇게 생성자가 딱 1개만 있으면 스프링이 해당 생성자에 `@Autowired` 로 의존관계를 주입해준다.
     * 따라서 **final 키워드를 빼면 안된다!**, 그러면 `ItemRepository` 의존관계 주입이 안된다.
     */
    private final ItemRepository itemRepository;

    @GetMapping //상품 목록
    public String items(Model model) {
        log.info("log info : items , method : get");
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    /**
     * 테스트용 데이터가 없으면 회원 목록 기능이 정상 동작하는지 확인하기 어렵다.
     * `@PostConstruct` : 해당 빈의 의존관계가 모두 주입되고 나면 초기화 용도로 호출된다.
     * 여기서는 간단히 테스트용 테이터를 넣기 위해서 사용했다.
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("ItemA", 1000, 10));
        itemRepository.save(new Item("ItemB", 2000, 10));
    }

    /**
     * th:object="${item}"` : `<form>` 에서 사용할 객체를 지정한다. 선택 변수 식( `*{...}` )을 적용할 수 있다.
     * `th:field="*{itemName}"`
     * `*{itemName}` 는 선택 변수 식을 사용했는데, `${item.itemName}` 과 같다. 앞서 `th:object` 로
     * `item` 을 선택했기 때문에 선택 변수 식을 적용할 수 있다.
     * `th:field` 는 `id` , `name` , `value` 속성을 모두 자동으로 만들어준다.
     * `id` : `th:field` 에서 지정한 변수 이름과 같다. `id="itemName"`
     * `name` : `th:field` 에서 지정한 변수 이름과 같다. `name="itemName"`
     * `value` : `th:field` 에서 지정한 변수의 값을 사용한다. `value=""`
     * 폼 안에서만 유효한 객체이다
     * th:object로 설정한 객체는 '파라미터'를 받아서 서버로 가져오는 역할을 한다. -> 쿼리 파라미터..
     */
    @GetMapping("/add") //상품 등록 폼 보여만 주기.
    public String addForm(Model model) {
        log.info("log info : create items method = get");
        model.addAttribute("itemForm", new ItemForm());

        return "basic/addForm";
    }

    /**
     * 상품 등록 처리 이후에 뷰 템플릿이 아니라 상품 상세 화면으로 리다이렉트 하도록 코드를 작성해보자.
     * 이런 문제 해결 방식을 `PRG Post/Redirect/Get` 라 한다.
     */

    /**
     * **RedirectAttributes**
     * `RedirectAttributes` 를 사용하면 URL 인코딩도 해주고, `pathVariable` , 쿼리 파라미터까지 처리해준다.
     * `redirect:/basic/items/{itemId}`
     * pathVariable 바인딩: `{itemId}`
     * 나머지는 쿼리 파라미터로 처리: `?status=true`
     */

    /**
     * 체크 박스 체크**
     * `open=on&_open=on`
     * 체크 박스를 체크하면 스프링 MVC가 `open` 에 값이 있는 것을 확인하고 사용한다. 이때 `_open` 은 무시한다.
     * **체크 박스 미체크**
     * `_open=on`
     * 체크 박스를 체크하지 않으면 스프링 MVC가 `_open` 만 있는 것을 확인하고, `open` 의 값이 체크되지 않았다고 인식한
     * 다.
     * 이 경우 서버에서 `Boolean` 타입을 찍어보면 결과가 `null` 이 아니라 `false` 인 것을 확인할 수 있다.
     * `log.info("item.open={}", item.getOpen());`
     */
    @PostMapping("/add") //등록하기 쿼리파라미터를 @ModelAttribute로 받는다. 객체의 필드명이 쿼리 파리미터명이랑 같아야 한다.
    public String addItemV6(@ModelAttribute ItemForm itemForm, RedirectAttributes redirectAttributes) {
        log.info("log info : addItemV6 method = post");
        Item item = new Item(itemForm.getName(), itemForm.getPrice(), itemForm.getQuantity());
        item.setOpen(itemForm.getOpen());
        item.setItemType(itemForm.getItemType());
        item.setSelectedRegions(itemForm.getSelectedRegions());
        item.setDeliveryCode(itemForm.getDeliveryCode());

        log.info("log info : item.open={}", itemForm.getOpen());
        log.info("itemForm.getRegions()={}", itemForm.getSelectedRegions());
        log.info("itemForm.getItemType()={}", itemForm.getItemType());
        log.info("itemForm.getDeliveryCode()={}", itemForm.getDeliveryCode());

        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}"; //저장하고 저장 객체의 정보를 모델에 담아 상품상세 뷰로 이동
    }

    /**
     * 상품등록을 하고 상품 상세를 get으로 다시 요청한 거랑 똑같다.
     * 리다이렉트하고 그 뷰까지 redirectAttribute가 값을 가져간다. 그 뷰에서 파라미터 status를 이용한다.
     * `${param.status}` : 타임리프에서 쿼리 파라미터를 편리하게 조회하는 기능
     * 원래는 컨트롤러에서 모델에 직접 담고 값을 꺼내야 한다. 그런데 쿼리 파라미터는 자주 사용해서 타임리프
     * 에서 직접 지원한다.
     */


    @GetMapping("/{itemId}")//상품 상세 Gradle로 빌드해야 PathVariable value속성 생략가능핮다.
    public String itemDetail(@PathVariable Long itemId, Model model) {
        log.info("log info : Detail items method = get");
        Item findItem = itemRepository.findById(itemId); //DTO 사용해보기
        model.addAttribute("item", findItem);
        return "basic/item";
    }

    @GetMapping("/{itemId}/edit")//상품 수정 폼 보여주기(원래의 상태를 표시하기 위해 뷰로 모델을 넘긴다.)
    public String updateForm(@PathVariable Long itemId, Model model) {
        log.info("log info : updateForm method = get");
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute ItemForm itemForm) {
        log.info("log info : updateItem method = post");
        log.info("itemForm.getRegions()={}", itemForm.getSelectedRegions());
        log.info("itemForm.getItemType()={}", itemForm.getItemType());
        itemRepository.update(itemId, itemForm.getName(), itemForm.getPrice(), itemForm.getQuantity()
                , itemForm.getOpen(), itemForm.getSelectedRegions(), itemForm.getItemType(), itemForm.getDeliveryCode());
        return "redirect:/basic/items/" + itemId;
    }


    /**
     * **@ModelAttribute의 특별한 사용법**
     * 등록 폼, 상세화면, 수정 폼에서 모두 서울, 부산, 제주라는 체크 박스를 반복해서 보여주어야 한다. 이렇게 하려면 각각
     * 의 컨트롤러에서 `model.addAttribute(...)` 을 사용해서 체크 박스를 구성하는 데이터를 반복해서 넣어주어야
     * 한다.
     * `@ModelAttribute` 는 이렇게 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
     * 이렇게하면 해당 컨트롤러를 요청할 때 `regions` 에서 반환한 값이 자동으로 모델( `model` )에 담기게 된다.
     * 물론 이렇게 사용하지 않고, 각각의 컨트롤러 메서드에서 모델에 직접 데이터를 담아서 처리해도 된다.
     * 이 컨트롤러를 호출할 때는 모델에 무조건 담긴다.
     */
    //성능 최적화 다른 클래스에 담아 놓고 static으로 호출하는 거 생각해보기
    @ModelAttribute("regions")
    public LinkedHashMap<String, String> region() {
        return Properties.getRegions();
    }

    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes(){
        return Properties.getDeliveryCodes();
    }

    @ModelAttribute("types")
    public List<ItemType> itemTypes() {
        return Arrays.asList(ItemType.values());
    }
}
