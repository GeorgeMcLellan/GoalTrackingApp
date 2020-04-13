package com.development.georgemcl.goaltracker.utils

import com.development.georgemcl.goaltracker.model.Action

fun Action.isProgressCompleted() = this.isRepeatAction && this.repeatProgressAmount >= this.repeatAmount