package cn.cooper.support.activitylauncher.sample;

import java.io.Serializable;

public class UserSerializable implements Serializable {

    private String id;
    private String name;

    public UserSerializable(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserSerializable{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
