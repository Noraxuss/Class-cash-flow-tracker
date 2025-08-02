package cash_flow.context;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
@Setter
public class AppContext {

    private final OverseerContext overseerContext;
    private final GroupContext groupContext;
    private final StageContext stageContext;
    private final NextSceneContext nextSceneContext;
    private final LoadingSceneContext loadingSceneContext;

    @Autowired
    public AppContext(OverseerContext overseerContext, GroupContext groupContext, StageContext stageContext, NextSceneContext nextSceneContext, LoadingSceneContext loadingSceneContext) {
        this.overseerContext = overseerContext;
        this.groupContext = groupContext;
        this.stageContext = stageContext;
        this.nextSceneContext = nextSceneContext;
        this.loadingSceneContext = loadingSceneContext;
    }
}
