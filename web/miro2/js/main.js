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

/*  This function is to select all options in a multi-valued <select> */
function selectAll(elementId) {
  var element = document.getElementById(elementId);
  len = element.length;
  if (len != 0) {
    for (i = 0; i < len; i++) {
      element.options[i].selected = true;
    }
  }
}

//picklist select all
function onFormSubmit(theForm) {
  selectAll('selectedProjects');
  return true;
}


function miroConfirm(mess,callback) {
  $("#confirmAlertBody").text(mess);

  $('#confirmAlertCloseButton').click(function () {
     callback(false);
  });

  $('#confirmAlertOkButton').click(function () {
    callback(true);
  });

  $('#confirmAlert').modal('show')

  return;
};

