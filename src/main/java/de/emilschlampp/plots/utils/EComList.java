package de.emilschlampp.plots.utils;

import java.util.ArrayList;
import java.util.List;

public class EComList {
    private final ArrayList<String> list = new ArrayList<>();
    public EComList(String s) {
        if(s.contains(", ")) {
            for (String a : s.split("\\,\\ ")) {
                list.add(a);
            }
        } else {
            for (String a : s.split(",")) {
                list.add(a);
            }
        }
    }

    public EComList(List<String> list) {
        for(String s : list) {
            this.list.add(s);
        }
    }

    public EComList(List list, boolean a) {
        for(Object s : list) {
            if(a) {
                this.list.add(s.toString());
            } else {
                this.list.add(String.valueOf(s));
            }
        }
    }

    public EComList() {
        this("");
    }

    @Override
    public String toString() {
        String a = "";
        for(String s : list) {
            a = a.replace("-njsdd__--", ", ")+s+"-njsdd__--";
        }
        a = a.replace("-njsdd__--", "");
        return a;
    }


    public void add(String o) {
        if(!list.contains(o)) {
            list.add(o);
        }
    }

    public boolean contains(String o) {
        return list.contains(o);
    }

    public void remove(String o) {
        while (list.remove(o));
    }

    public List<String> toList() {
        return new ArrayList<>(list);
    }
}
