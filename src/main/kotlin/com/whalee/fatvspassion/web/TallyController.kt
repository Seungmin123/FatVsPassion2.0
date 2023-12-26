package com.whalee.fatvspassion.web

import com.whalee.fatvspassion.module.tally.TallyService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tally")
class TallyController constructor(var tallyService: TallyService){


}