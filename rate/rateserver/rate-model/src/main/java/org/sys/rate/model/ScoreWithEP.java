package org.sys.rate.model;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Data
public class ScoreWithEP {
    private Integer expertID;
    private Integer participantID;
    private List<ScoreItem> scoreList;
}
