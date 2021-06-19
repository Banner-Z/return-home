package com.lxw.system.service.impl;

import com.lxw.entity.Task;
import com.lxw.system.mapper.TaskMapper;
import com.lxw.system.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lxw
 * @since 2021-03-16
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

}
