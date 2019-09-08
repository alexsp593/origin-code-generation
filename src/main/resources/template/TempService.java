package template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${entityName};
import ${mapperFullName};
import ${packagePath}base.BaseService;

/**
 * ${tableNote} Service
 *
 * @author ${author}
 * @date ${systemDate}
 */
@Service("$!{lowerName}Service")
public class ${className}Service extends BaseService<${className}>{
    private static final Logger logger = LoggerFactory.getLogger(${className}Service.class);
    @Autowired
    private ${className}Mapper mapper;

    @Override
    public ${className}Mapper getMapper(){
        return mapper;
    }
}
