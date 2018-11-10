package com.example.tomizzje.garagecross.enums;

public enum FoodGroups {
    HÚSOK_VEGETÁRIÁNUSOK{
        @Override
        public String getString(){
            return "Húsok és vegetáriánusok";
        }
    }, LEVÉLZÖLDSÉGEK{
        @Override
        public String getString(){
            return "Levélzöldségek";
        }
    }, KEMÉNYÍTŐ_TARTALMÚ_ZÖLDSÉGEK{
        @Override
        public String getString(){
            return "Keményítő tartalmú zöldségek";
        }
    },GABONAFÉLÉK{
        @Override
        public String getString(){
            return "Gabonafélék";
        }
    },GYÜMÖLCSÖK{
        @Override
        public String getString(){
            return "Gyümölcsök";
        }
    },OLAJOK{
        @Override
        public String getString(){
            return "Olajok";
        }
    },TEJTERMÉKEK{
        @Override
        public String getString(){
            return "Tejtermékek";
        }
    },MAGVAK_ÉS_DIÓFÉLÉK{
        @Override
        public String getString(){
            return "Magvak és diófélék";
        }
    },HÜVELYESEK{
        @Override
        public String getString(){
            return "Hüvelyesek";
        }
    };



    public abstract String getString();
}
