package test.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ClickhouseTestMapper {
    Integer testSelect();
}
