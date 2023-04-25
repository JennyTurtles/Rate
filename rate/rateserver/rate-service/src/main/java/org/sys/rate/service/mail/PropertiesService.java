package org.sys.rate.service.mail;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PropertiesService {

    private String host;
    private String username;
    private String password;
    private String sendHost;

    private Map<String, Object> cachedProperties; // 添加一个 Map 来保存检索的数据

    public String getSendHost() {
        return sendHost;
    }

    public void setSendHost(String sendHost) {
        this.sendHost = sendHost;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        if(cachedProperties != null && cachedProperties.get("username") != null) {
            return (String) cachedProperties.get("username"); // 从缓存中获取
        } else {
            return null;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        if(cachedProperties != null && cachedProperties.get("password") != null) {
            return (String) cachedProperties.get("password"); // 从缓存中获取
        } else {
            return null;
        }
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void setMyPropertyFromDatabase() {
        String sql = "SELECT host, username, password, sendHost FROM properties WHERE id = (SELECT MAX(id) FROM properties)";
        cachedProperties = jdbcTemplate.queryForMap(sql);

        setHost((String) cachedProperties.get("host"));
        setUsername((String) cachedProperties.get("username"));
        setPassword((String) cachedProperties.get("password"));
        setSendHost((String) cachedProperties.get("sendHost"));
    }

    public PropertiesService() {

    }
}
