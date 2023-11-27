package com.dev.frequenc.ui_codes.data.req

class ProfilePicReq (
   var fieldname : String,
   var originalname : String,
   var buffer : ByteArray,
   var encoding : String = "7bit",
   var mimetype : String = "image/jpeg",
)

