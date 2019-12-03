package course_at_mobile.step4.screens;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;


// Класс для работы с разделом\экраном поиска
public class AppSearchScreen extends BaseScreen {

    public static final By
            SEARCH_FIELD_BY = By.id("org.wikipedia:id/search_src_text"),
            SEARCH_CLOSE_BY = By.id("org.wikipedia:id/search_close_btn"),
            LIST_ELEMENT_BY = By.id("org.wikipedia:id/page_list_item_container"),
            LIST_ITEM_TITLE_BY = By.id("org.wikipedia:id/page_list_item_title"),
            ADD_LIST_BY = By.xpath("//*[@text='Add to reading list']"),
            GO_IT_BY = By.id("org.wikipedia:id/onboarding_button"),
            CREATE_NEW_BY = By.id("org.wikipedia:id/create_button"),
            NAME_NEW_LIST_BY = By.id("org.wikipedia:id/text_input"),
            OK_BUTTON_BY = By.xpath("//*[@text='OK']");

    public AppSearchScreen(AppiumDriver appiumDriver) {
        super(appiumDriver);
    }

    public AppMainScreen clickBlackAndReturmMainScreen() {
        findAndGetElement(By.className("android.widget.ImageButton")).click();
        return new AppMainScreen(appiumDriver);
    }

    public void setTextForSearch(String textForSearch) {
        var searchField = findAndGetElement(SEARCH_FIELD_BY);
        searchField.sendKeys(textForSearch);

        // Ждем появления хотя бы одного результата поиска
        waitForElementPresent(LIST_ELEMENT_BY);
    }

    public List<WebElement> getListWebElementsResult() {
        return findAndGetListElements(LIST_ITEM_TITLE_BY);
    }

    public AppArticleScreen openArticleByNumberResult(Integer number) {
        if (number <= 0) throw new AssertionError("Номер статьи для выбора должен быть больше нуля");
        var listResult = getListWebElementsResult();

        if (listResult.size() < number)
            throw new AssertionError("Нельзя выбрать статью, тк результатов меньше: " + listResult.size());

        listResult.get(number - 1).click();

        return new AppArticleScreen(appiumDriver);
    }

    public WebElement findByTextAndReturnWebElement(String textForSearch) {
        var listResult = getListWebElementsResult();

        for (WebElement element : listResult) {
            if (element.getText().equals(textForSearch))
                return element;
        }
        throw new AssertionError("Ничего не нашлось из результата поиска");
    }


    public AppMainScreen clickCancelSearchAndReturmMainScreen() {
        var closeButton = findAndGetElement(SEARCH_CLOSE_BY);
        closeButton.click();

        return new AppMainScreen(appiumDriver);
    }

    // Выпадающее меню при долгом табе у элемента поиска
    public void openMenuToElement(WebElement webElement) {
        longTapToElement(webElement);
    }

    public void clickAddToReadingList() {
        findAndGetElement(ADD_LIST_BY).click();
    }

    // элемент онбординга, появляется один раз после чистой установки
    public void clickGotIt() {
        findAndGetElement(GO_IT_BY).click();
    }

    public void clickCreateNew() {
        findAndGetElement(CREATE_NEW_BY).click();
    }

    public void setNewNameListAndClickOk(String newName) {
        var inputNameList = findAndGetElement(NAME_NEW_LIST_BY);
        inputNameList.click();
        inputNameList.clear();
        inputNameList.sendKeys(newName);
        findAndGetElement(OK_BUTTON_BY).click();
    }

    public void clickSaveToReadingList(String nameList) {
        try {
            var listForSave = findAndGetElement(getLocatorForSaveToReadingList(nameList));
            listForSave.click();
        } catch (TimeoutException err) {
            throw new AssertionError("Не нашелся искомый список для добавления ссылки");
        }
    }

    private By getLocatorForSaveToReadingList(String nameList) {
        return By.xpath("//android.widget.LinearLayout//android.widget.TextView[@text='{NAME_LIST}']".replace("{NAME_LIST}", nameList));
    }

}