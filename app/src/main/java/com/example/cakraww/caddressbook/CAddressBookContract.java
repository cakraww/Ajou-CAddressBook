package com.example.cakraww.caddressbook;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cakraww on 7/15/16.
 */
public final class CAddressBookContract {
    public static final String TABLE_NAME = "contacts";
    public static final String COL_NAME = "name";
    public static final String COL_PHONE = "phone";
    public static final String COL_COMPANY = "company";
    public static final String COL_CATEGORY = "category";

    public enum Category {
        FAMILY("Family"), WORK("Work"), FRIEND("Friend");

        private final String display;

        Category(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }

        public static List<CharSequence> getCategories() {
            List<CharSequence> result = new ArrayList<>();
            for (Category category : Category.values()) {
                result.add(category.getDisplay());
            }
            return result;
        }

        public static Category fromString(String categoryStr) {
            for (Category category : Category.values()) {
                if (category.getDisplay().equals(categoryStr)) return category;
            }
            throw new IllegalArgumentException("no category found");
        }
    }


    private CAddressBookContract() {
    }
}
