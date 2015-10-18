window.alert = function(mess) {
  $("#warning-alert-mess").text(mess);
  $("#warning-alert").alert();
  $("#warning-alert").fadeTo(2000, 500).slideUp(500, function(){
    $("#success-alert").alert('close');
  })
  return;
};

function closeAlert()  {
  $("#success-alert").alert('close');
}

function setPlaceHolder(fieldId,placeHolderValue) {
  console.log(fieldId);
  console.log(placeHolderValue);
  $("#"+fieldId).attr("placeholder", placeHolderValue);
  return;
}