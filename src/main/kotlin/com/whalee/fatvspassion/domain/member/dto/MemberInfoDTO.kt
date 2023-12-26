package com.whalee.fatvspassion.domain.member.dto

import com.whalee.fatvspassion.domain.member.Member

data class MemberInfoDTO (var member: Member){
    var id: Long = member.id
    var name: String = member.name
    var role: String = member.role
}