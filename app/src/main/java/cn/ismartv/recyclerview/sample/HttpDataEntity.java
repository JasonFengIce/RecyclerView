package cn.ismartv.recyclerview.sample;

import java.util.List;

/**
 * Created by huaijie on 9/11/15.
 */
public class HttpDataEntity {
    private String count;
    private List<Object> objects;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    class Object {

        private String image;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
