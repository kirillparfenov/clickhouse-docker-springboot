package test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import test.mappers.ClickhouseTestMapper;

@Service
public class ClickhouseService {
    private static final Logger log = LoggerFactory.getLogger(ClickhouseService.class);

    private final ClickhouseTestMapper clickhouseTestMapper;

    @Autowired
    public ClickhouseService(final ClickhouseTestMapper clickhouseTestMapper) {
        this.clickhouseTestMapper = clickhouseTestMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testSelect() {
        log.info("Тестовый запрос в клик: {}", clickhouseTestMapper.testSelect());
    }
}
