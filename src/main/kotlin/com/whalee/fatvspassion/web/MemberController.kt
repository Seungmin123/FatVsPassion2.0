package com.whalee.fatvspassion.web

import com.whalee.fatvspassion.domain.member.dto.MemberInfoDTO
import com.whalee.fatvspassion.module.member.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController (var memberService: MemberService){


    @GetMapping("/{id}")
    fun getMemberInfo(@PathVariable id: Long): MemberInfoDTO = memberService.getMemberInfo(id)

//    @PostMapping("/complete")
//    fun exerciseComplete(@RequestBody memberExerciseDTO: MemberExerciseDTO) = memberService.memberExerciseComplete(memberExerciseDTO)
}