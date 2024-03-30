package domain.util

open class StarTouchException(override val message: String?) : Exception(message)

open class UnAuthException(override val message: String?) : StarTouchException(message)

class NetworkException(override val message: String?) : StarTouchException(message)

class PermissionDeniedException(override val message: String?) : UnAuthException(message)

class NotFoundException(override val message: String?) : StarTouchException(message)

class ValidationNetworkException(override val message: String?) : StarTouchException(message)

open class ValidationException(override val message: String?) : StarTouchException(message)

class EmptyDataException(override val message: String?) : StarTouchException(message)

class UnknownErrorException(override val message: String?) : StarTouchException(message)

class ServerErrorException(override val message: String?) : StarTouchException(message)