// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        Set<String> requestedAttendees = request.getAttendees();
        long requestedDuration = request.getDuration();
        
        if (requestedDuration > TimeRange.WHOLE_DAY.duration()) {
            return new ArrayList();
        }
        
        if (events.isEmpty()) {
            return Arrays.asList(TimeRange.WHOLE_DAY);
        }
        
        return findQualifyingTimeRanges(requestedAttendees, events, requestedDuration);
        
        
    }

    private Collection<TimeRange> findQualifyingTimeRanges(Set<String> requestedAttendees, Collection<Event> events, long requestedDuration) {
        Collection<TimeRange> qualifyingTimeRanges = new ArrayList();
        List<Event> eventsList = events.stream().sorted(Event.ORDER_BY_START).collect(Collectors.toList());
        int nextAvailiableTime = TimeRange.START_OF_DAY;
        for(Event event : events){
            int eventStartTime = event.getWhen().start();
            int eventEndTime = event.getWhen().end();
            if(!Collections.disjoint(event.getAttendees(), requestedAttendees)){
                if(nextAvailiableTime + requestedDuration <= eventStartTime){
                    qualifyingTimeRanges.add(TimeRange.fromStartEnd(nextAvailiableTime, eventStartTime, false));
                }
                nextAvailiableTime = Math.max(eventEndTime, nextAvailiableTime);
            }
        }
        if (nextAvailiableTime + requestedDuration <= TimeRange.END_OF_DAY) {
            qualifyingTimeRanges.add(TimeRange.fromStartEnd(nextAvailiableTime, TimeRange.END_OF_DAY, true));
        }
        return qualifyingTimeRanges;
    }
}