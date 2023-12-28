//import org.assertj.core.api.Assertions.assertThat
//import org.bouncycastle.oer.its.ieee1609dot2.EndEntityType.app
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.openqa.selenium.By
//import org.openqa.selenium.JavascriptExecutor
//import org.openqa.selenium.WebDriver
//import org.openqa.selenium.chrome.ChromeDriver
//import org.openqa.selenium.support.ui.ExpectedConditions
//
//class AddUserTest {
//    private var driver: WebDriver? = null
//    private var vars: Map<String, Any>? = null
//    var js: JavascriptExecutor? = null
//
//    @BeforeEach
//    fun setUp() {
//        driver = ChromeDriver()
//        js = driver as JavascriptExecutor?
//        vars = HashMap()
//    }
//
//    @AfterEach
//    fun tearDown() {
//        driver?.quit()
//    }
//
//    @Test
//    fun addUser() {
//        JavalinTest.test(app) { _, client ->
//
//            //Navigate to the home page and assert that the title is displayed.
//            driver.get("${client.origin}/")
//            assertThat(driver.pageSource).contains("<title>HealthTracker</title>")
//
//            //Navigate to the user overview page and verify that it was successful
//            driver.findElement(By.linkText("More Details...")).click()
//            assertThat(driver.pageSource).contains("<template id=\"user-overview\">")
//
//            //Click on the Add button to expand the add user form
//            driver.findElement(By.cssSelector("button[title='Add']")).click()
//
//            //Enter details into the name and email fields
//            driver.findElement(By.name("name")).click();
//            driver.findElement(By.name("name")).sendKeys("Lisa J Simpson");
//            driver.findElement(By.name("email")).click();
//            driver.findElement(By.name("email")).sendKeys("lisaj@simpson.com");
//
//            //Click on the Add button to add the new user to the database
//            driver.findElement(By.cssSelector("button[title='AddUser']")).click()
//
//            //Verify we are still on the user overview page
//            assertThat(driver.pageSource).contains("<template id=\"user-overview\">")
//
//            // Wait for the presence of list-group-item(s) by checking if at least one exists
//            wait!!.until(ExpectedConditions.presenceOfElementLocated(By.name("list-group")))
//            wait!!.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.name("list-group-item"), 0))
//
//            //Click on the new user and verify we are brought to the user profile page
//            driver.findElement(By.linkText("Lisa J Simpson (lisaj@simpson.com)")).click()
//            assertThat(driver.pageSource).contains("<template id=\"user-profile\">")
//
//            wait!!.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button[title='Delete']")))
//            driver.findElement(By.cssSelector("button[title='Delete']")).click()
//            //driver.findElement(By.cssSelector(".list-group-item:nth-child(8) .fas")).click()
//            assertThat(
//                    driver.switchTo().alert().text
//            ).isEqualTo("Do you really want to delete?")
//            driver.switchTo().alert().accept()
//        }
//    }
//}