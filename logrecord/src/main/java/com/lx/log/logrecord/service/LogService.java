package com.lx.log.logrecord.service;

import com.lx.log.logrecord.model.LogDTO;

public interface LogService {

    boolean createLog(LogDTO logDTO) throws Exception;

}
