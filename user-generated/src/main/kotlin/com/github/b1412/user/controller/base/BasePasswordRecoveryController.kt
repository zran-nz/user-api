package com.github.b1412.user.controller.base

import com.github.b1412.api.controller.BaseController
import org.springframework.web.bind.annotation.RestController
import com.github.b1412.user.entity.PasswordRecovery
import com.github.b1412.json.GraphRender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RequestParam


@Transactional
abstract class BasePasswordRecoveryController : BaseController<PasswordRecovery, Long>() {

@GraphRender("passwordRecovery")
@GetMapping
override fun page(request: HttpServletRequest, @RequestParam filter: Map
<String, String>, pageable: Pageable): ResponseEntity<*> {
return super.page(request, filter,pageable)

}

@GraphRender("passwordRecovery")
@GetMapping("{id}")
override fun findOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
return super.findOne(id, request)
}

@PostMapping
override fun saveOne(@Validated @RequestBody input: PasswordRecovery, request: HttpServletRequest): ResponseEntity<*> {
return super.saveOne(input, request)

}

@PutMapping("{id}")
override fun updateOne(@PathVariable id: Long, @Validated @RequestBody input: PasswordRecovery, request: HttpServletRequest): ResponseEntity<*> {
super.updateOne(id, input, request)
return ResponseEntity.noContent().build<PasswordRecovery>()
}

@DeleteMapping("{id}")
override fun deleteOne(@PathVariable id: Long, request: HttpServletRequest): ResponseEntity<*> {
return super.deleteOne(id,request)

}
}
