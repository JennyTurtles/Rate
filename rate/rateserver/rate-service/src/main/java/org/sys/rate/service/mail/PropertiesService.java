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
        if(!username.isEmpty()) {
            return username;
        } else {
            return null;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;


    public void setMyPropertyFromDatabase() {
        String sql = "SELECT host, username, password, sendHost FROM properties WHERE id = (SELECT MAX(id) FROM properties)";
        Map<String, Object> row = jdbcTemplate.queryForMap(sql);

        setHost((String) row.get("host"));
        setUsername((String) row.get("username"));
        setPassword((String) row.get("password"));
        setSendHost((String) row.get("sendHost"));
    }

    public PropertiesService() {
        boolean flag = false;
        if(flag) {
            throw new NullPointerException();
        }
    }

}
