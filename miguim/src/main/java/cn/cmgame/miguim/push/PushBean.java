package cn.cmgame.miguim.push;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Amuro on 2017/8/28.
 */

/**
 *  {"module":"0001",
 *  "data":"{\"title\":\"测试消息标题：1503901861274\",
 *  \"content\":\"测试消息内容：cebef204-1019-47c8-b02e-a45f780b7605\",
 *  \"link\":\"https://g.10086.cn/h/b9009472-705e-43f8-918b-3e7b2090bd05\",
 *  \"imgPath\":\"https://g.10086.cn/download/images/5cde6b7a-c6a2-4faf-87ac-bdda2923494b.jpg\",
 *  \"icon\":\"https://g.10086.cn/download/icons/dd95037b-5fb7-4612-8887-98e9005b5336.jpg\",
 *  \"description\":\"剽悍的人生不需要解释\",
 *  \"id\":\"665482070\",
 *  \"bindings\":[\"41262008\",\"10011000\",\"19030086\"],
 *  \"provisions\":[\"696816044768\"]}"}
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
    private List<String> bindings;
    private List<String> provisions;

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

    public List<String> getChannels()
    {
        return bindings;
    }

    public void setBindings(List<String> bindings)
    {
        this.bindings = bindings;
    }

    public List<String> getContents()
    {
        return provisions;
    }

    public void setProvisions(List<String> provisions)
    {
        this.provisions = provisions;
    }
}
