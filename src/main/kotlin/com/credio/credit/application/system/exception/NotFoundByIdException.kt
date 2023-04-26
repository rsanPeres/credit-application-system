package com.credio.credit.application.system.exception

data class NotFoundByIdException(override val message : String?) : RuntimeException(message)
