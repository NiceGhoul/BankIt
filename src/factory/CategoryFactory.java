package factory;

import java.util.ArrayList;
import model.Category;

public class CategoryFactory {
    private static int categoryIdCounter = 1;
    private static ArrayList<Category> categoryList = new ArrayList<>();
    public static void createCategory(String categoryName){
        Category category = new Category(categoryIdCounter++, categoryName);
        categoryList.add(category);
    }
    public static ArrayList<Category> getCategoryList() {
        createDummyCategories();
        return categoryList;
    }
    public static void createDummyCategories() {
        createCategory("Groceries");
        createCategory("Utilities");
        createCategory("Transportation");
        createCategory("Entertainment");
        createCategory("Healthcare");
        createCategory("Education");
        createCategory("Dining Out");
        createCategory("Savings");
        createCategory("Investments");
        createCategory("Clothing");
    }
    
}
