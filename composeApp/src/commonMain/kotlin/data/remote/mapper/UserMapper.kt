package data.remote.mapper

import data.remote.model.UserDto
import domain.entity.User
import util.getDateNow

fun User.toUserDto(): UserDto = UserDto(
    name = name,
    code = code,
    password = password,
    userName = userName,
    userClassID = userClassID,
    myLanguage = language,
    title = title,
    fullName = fullName,
    address = address,
    jop = jop,
    mobile = mobile,
    email = email,
    passCode = passCode,
    phone = phone,
    active = active,
    createDate = createDate,
    modifiedDate = modifiedDate,
    userID = adminID,
    restIDActive = restIDActive,
    isWaiter = isWaiter,
)


fun UserDto.toEntity(): User = User(
    name = name ?: "",
    code = code ?: 0,
    password = password ?: "",
    userName = userName ?: "",
    userClassID = userClassID ?: 0,
    language = myLanguage ?: "",
    title = title ?: "",
    fullName = fullName ?: "",
    address = address,
    jop = jop,
    mobile = mobile,
    email = email,
    passCode = passCode ?: "",
    phone = phone,
    active = active ?: false,
    createDate = createDate ?: getDateNow(),
    modifiedDate = modifiedDate ?: getDateNow(),
    adminID = userID ?: 0,
    restIDActive = restIDActive ?: 0,
    isWaiter = isWaiter ?: false,
)