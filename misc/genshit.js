var _ = require("underscore");
var first = ["A","E","D","O"]
var lexmex  = ["LEX","MEX","HEX","LIN","MIN","HIN"]
var SNTF = ["S","N","T","F"]


first.forEach(function(l0) {

  var second = _.without(first,l0)


  second.forEach(function(l1) {

    var third = _.without(second,l1)

    third.forEach(function(l2) {


      lexmex.forEach(function(lexmex) {

        var l = SNTF[_.random(0, (SNTF.length -1))]

        console.log("extroIntraMapping.put(\""+l0 + l1 + l2 + lexmex + "\",\"" + l +  lexmex + "\");")

      })



    })



  })




})