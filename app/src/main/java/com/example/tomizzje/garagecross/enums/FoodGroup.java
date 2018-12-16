package com.example.tomizzje.garagecross.enums;

public enum FoodGroup {
    MEATS_AND_VEGETARIANS("Hús- és halfélék"),
    GREEN_VEGGIES("Levélzöldségek"),
    STARCHY_VEGGIES("Keményítő tartalmú zöldségek"),
    GRAINS("Gabonafélék"),
    FRUITS("Gyümölcsök"),
    OILS("Olajok"),
    DAIRY("Tejtermékek"),
    NUTS_SEEDS("Magvak és diófélék"),
    LEGUMES("Hüvelyesek");

    private final String userFriendlyString;

    FoodGroup(String userFriendlyString) {
        this.userFriendlyString = userFriendlyString;
    }

    @Override
    public String toString(){
        return userFriendlyString;
    }


    /**
     * This method return a foodgroup by userFriendlyString
     * @param userFriendlyString of the Foodgroup
     * @return FoodGroup
     */
    public static FoodGroup getFoodGroupByString(String userFriendlyString){
        for(FoodGroup f : FoodGroup.values()){
            if(f.userFriendlyString.equals(userFriendlyString)){
                return f;
            }
        }
        return MEATS_AND_VEGETARIANS;
    }

    /**
     * This method return a foodgroup by his name
     * @param name of the foodgroup
     * @return a FoodGroup
     */
    public static FoodGroup getFoodGroupByName(String name){
        for(FoodGroup f : FoodGroup.values()){
            if(f.name().equals(name)){
                return f;
            }
        }
        return MEATS_AND_VEGETARIANS;
    }

    /**
     * This method return FoodGroup String array
     * @return String array
     */
    public static String[] getFoodGroupValuesString(){
        FoodGroup[] temp = FoodGroup.values();
        String[] result = new String[temp.length];
        for(int i=0;i<temp.length;++i){
            result[i] = temp[i].toString();
        }
        return result;
    }
}
