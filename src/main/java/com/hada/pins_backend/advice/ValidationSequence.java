package com.hada.pins_backend.advice;

import com.hada.pins_backend.advice.ValidationGroups.*;
import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({NotEmptyGroup.class, SizeCheckGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {
}

