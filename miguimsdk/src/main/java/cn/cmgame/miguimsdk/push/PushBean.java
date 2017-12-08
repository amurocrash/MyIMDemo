package cn.cmgame.miguimsdk.push;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Amuro on 2017/8/28.
 */

/**
 *  '{
 *  "id":5
 *  "title":"测试消息标题：1512633020299",
 *  "content":"测试消息内容：3eebffca-ed5c-4d70-961c-62f40c0638fa",
 *  "link":"https://g.10086.cn/h/7b148889-3b49-402b-84ff-12cbe4730c50",
 *  "imgPath":"https://g.10086.cn/download/images/b5aca12b-950b-45f1-8e55-a6ee9f1debb1.jpg",
 *  "icon":"https://g.10086.cn/download/icons/53ea8e1d-cb59-4793-8130-b41a7539a512.jpg",
 *  "description":"剽悍的人生不需要解释"}'
 */
public class PushBean implements Serializable
{
    private String id;
    private String title;
    private String content;
    private String link;
    private String imgPath;
    private String icon;
    private String description;
//    private List<String> bindings;
//    private List<String> provisions;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getImgPath()
    {
        return imgPath;
    }

    public void setImgPath(String imgPath)
    {
        this.imgPath = imgPath;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

//    public List<String> getChannels()
//    {
//        return bindings;
//    }
//
//    public void setBindings(List<String> bindings)
//    {
//        this.bindings = bindings;
//    }
//
//    public List<String> getContents()
//    {
//        return provisions;
//    }
//
//    public void setProvisions(List<String> provisions)
//    {
//        this.provisions = provisions;
//    }
}
