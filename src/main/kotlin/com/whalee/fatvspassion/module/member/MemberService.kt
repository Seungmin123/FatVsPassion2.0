package com.whalee.fatvspassion.module.member

import com.whalee.fatvspassion.domain.member.Member
import com.whalee.fatvspassion.domain.member.MemberRepository
import com.whalee.fatvspassion.domain.member.dto.MemberExerciseDTO
import com.whalee.fatvspassion.domain.member.dto.MemberInfoDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional
class MemberService constructor(var memberRepository: MemberRepository){

    fun getMemberInfo(id: Long): MemberInfoDTO =  MemberInfoDTO(memberRepository.findById(id).getOrElse { throw Exception("no Member") })

    fun memberExerciseComplete(memberExerciseDTO: MemberExerciseDTO): String {

        return "COMPLETE"
    }
}