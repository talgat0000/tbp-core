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

package de.geobe.tbp.core.domain

import de.geobe.util.association.IGetOther
import de.geobe.util.association.IToAny
import de.geobe.util.association.ToOne

import javax.persistence.*

/**
 * Created by georg beier on 03.01.2018.
 */
@Entity // save entity class into DB
@Table(name="TBL_TASK") // optionally name the db table
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    String name
    String description
    TaskState state = TaskState.PLANNED
    /** to be replaced by an evaluation of milestones */
    Date completionDate = new Date() + 10
    Date scheduledCompletionDate
    /** shall be replaced by an association to planningData */
    Float timeBudget = 42.24

    @ManyToOne
    @JoinColumn(name = 'supertask_id')
    protected CompoundTask supertask
    @Transient
    private ToOne<Task, CompoundTask> toSupertask = new ToOne<>(
            { this.@supertask } as IToAny.IGet,
            { CompoundTask t -> this.@supertask = t } as IToAny.ISet,
            this, { CompoundTask ct -> ct.subtask } as IGetOther
    )
    IToAny<Task> getSupertask() { toSupertask }

    Long getId() { id }

    /**
     * get completion time, to be replaced by an evaluation of milestones
     */
//    Date getCompletionDate() {
//        new Date() + 10
//    }

    /**
     * get scheduled completion date, to be replaced by an evaluation of milestones
     */
//    Date getScheduledCompletionDate() {
//        scheduledCompletionDate
//    }

    /**
     * get the time assigned on this task
     * @return time in working hours
     */
//    Float getTimeBudget() {timeBudget}

    /**
     * get the time already spent on this task
     * @return time in working hours
     */
    abstract Float getTimeUsed()
}
