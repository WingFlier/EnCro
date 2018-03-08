package wingfly.com.encro.POJO_s;

import wingfly.com.encro.Constants;
import wingfly.com.encro.encryption.Encryptor;

public class Friend
{
    private String name, key;
    private int id;

    private Friend()
    {
    }

    public Friend(String name, String key)
    {
        this.name = name;
        this.key = key;
    }

    public Friend(String name, String key, int id)
    {
        this.name = name;
        this.key = key;
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}