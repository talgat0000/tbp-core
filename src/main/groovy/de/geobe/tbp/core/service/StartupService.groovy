/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018.  Georg Beier. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.geobe.tbp.core.service

import de.geobe.tbp.core.domain.CompoundTask
import de.geobe.tbp.core.domain.Project
import de.geobe.tbp.core.domain.Subtask
import de.geobe.tbp.core.repository.TaskRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

import java.time.LocalDateTime

/**
 * Created by georg beier on 04.01.2018.
 */
@Service
@Slf4j
class StartupService implements IStartupService {
    private boolean isInitialized = false

    @Autowired
    private TaskRepository taskRepository

    @Override
    void initApplicationData() {
        if (!taskRepository.findAll()) {
            log.info("initializing data at ${LocalDateTime.now()}")
            def proj = new Project([name: 'Prepare Lecture'])
            def prepsl = new CompoundTask([name: 'Prepare Slides'])
            def prepex = new CompoundTask([name: 'Prepare Example Code'])
            proj.subtask.add([prepsl, prepex])
            def exmon = new CompoundTask([name: 'Monolithic example'])
            def exmic = new CompoundTask([name: 'Microservice example'])
            def exspl = new Subtask([name                   : 'Simple Examples',
                                     scheduledCompletionDate: (new Date() + 5).clearTime()])
            prepex.subtask.add([exmon, exspl, exmic])
            def sl1 = new Subtask([name                   : 'Select Existing Slides',
                                   scheduledCompletionDate: (new Date() + 3).clearTime()])
            def sl2 = new Subtask([name                   : 'Translate Selected Existing Slides',
                                   scheduledCompletionDate: (new Date() + 6).clearTime()])
            def sl3 = new Subtask([name                   : 'Create New Slides',
                                   scheduledCompletionDate: (new Date() + 10).clearTime()])
            prepsl.subtask.add([sl1, sl2, sl3])
            def ex1 = new Subtask([name                   : 'Program Monolithic Examples',
                                   scheduledCompletionDate: (new Date() + 15).clearTime()])
            def ex2 = new Subtask([name                   : 'Test Monolithic Examples',
                                   scheduledCompletionDate: (new Date() + 17).clearTime()])
            def ex4 = new Subtask([name                   : 'Refactor Monolithic Examples',
                                   scheduledCompletionDate: (new Date() + 25).clearTime()])
            def ex3 = new Subtask([name                   : 'Install Docker',
                                   scheduledCompletionDate: (new Date() + 1).clearTime()])
            def ex5 = new Subtask([name                   : 'Test MMicroservice Examples',
                                   scheduledCompletionDate: (new Date() + 15).clearTime()])
            exmon.subtask.add([ex1,ex2])
            exmic.subtask.add([ex3, ex4, ex5])
            taskRepository.saveAndFlush(proj)
        }
    }

    @Override
    void cleanupAll() {

    }
}

/**
 * Let Spring Boot run the StartupService on any ContextRefreshed event, i.e at startup
 */
@Component
class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private IStartupService startupService

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        startupService.initApplicationData()
    }
}