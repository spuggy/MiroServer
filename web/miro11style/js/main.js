//fix for IE8 and less missing bind function on a function
if (!Function.prototype.bind) {
  Function.prototype.bind = function(oThis) {
    if (typeof this !== 'function') {
      // closest thing possible to the ECMAScript 5
      // internal IsCallable function
      throw new TypeError('Function.prototype.bind - what is trying to be bound is not callable');
    }

    var aArgs   = Array.prototype.slice.call(arguments, 1),
        fToBind = this,
        fNOP    = function() {},
        fBound  = function() {
          return fToBind.apply(this instanceof fNOP
                  ? this
                  : oThis,
              aArgs.concat(Array.prototype.slice.call(arguments)));
        };

    if (this.prototype) {
      // native functions don't have a prototype
      fNOP.prototype = this.prototype;
    }
    fBound.prototype = new fNOP();

    return fBound;
  };
}


//window.alert = function(mess) {
//  $("#warning-alert-mess").text(mess);
//  $("#warning-alert").alert();
//  $("#warning-alert").fadeTo(2000, 500).slideUp(500, function(){
//    $("#warning-alert").alert('close');
//  })
//  return;
//};

function closeAlert(id)  {
  $(id).fadeTo(2000, 500).slideUp(500, function(){
    $(id).alert('close');
  })
}

function setPlaceHolder(fieldId,placeHolderValue) {
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


/* This function is used to get cookies */
function getCookie(name) {
  var prefix = name + "="
  var start = document.cookie.indexOf(prefix)

  if (start==-1) {
    return null;
  }

  var end = document.cookie.indexOf(";", start+prefix.length)
  if (end==-1) {
    end=document.cookie.length;
  }

  var value=document.cookie.substring(start+prefix.length, end)
  return unescape(value);
}

/* This function is used to delete cookies */
function deleteCookie(name,path,domain) {
  if (getCookie(name)) {
    document.cookie = name + "=" +
        ((path) ? "; path=" + path : "") +
        ((domain) ? "; domain=" + domain : "") +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
}

// This function is for stripping leading and trailing spaces
function trim(str) {
  if (str != null) {
    var i;
    for (i=0; i<str.length; i++) {
      if (str.charAt(i)!=" ") {
        str=str.substring(i,str.length);
        break;
      }
    }

    for (i=str.length-1; i>=0; i--) {
      if (str.charAt(i)!=" ") {
        str=str.substring(0,i+1);
        break;
      }
    }

    if (str.charAt(0)==" ") {
      return "";
    } else {
      return str;
    }
  }
}

// This function is used by the login screen to validate user/pass
// are entered.
function validateRequired(form) {
  var bValid = true;
  var focusField = null;
  var i = 0;
  var fields = new Array();
  oRequired = new required();

  for (x in oRequired) {
    if ((form[oRequired[x][0]].type == 'text' || form[oRequired[x][0]].type == 'textarea' || form[oRequired[x][0]].type == 'select-one' || form[oRequired[x][0]].type == 'radio' || form[oRequired[x][0]].type == 'password') && form[oRequired[x][0]].value == '') {
      if (i == 0)
        focusField = form[oRequired[x][0]];

      fields[i++] = oRequired[x][1];

      bValid = false;
    }
  }

  if (fields.length > 0) {
    focusField.focus();
    alert(fields.join('\n'));
  }

  return bValid;
}


// This function is a generic function to create form elements
function createFormElement(element, type, name, id, value, parent) {
  var e = document.createElement(element);
  e.setAttribute("name", name);
  e.setAttribute("type", type);
  e.setAttribute("id", id);
  e.setAttribute("value", value);
  parent.appendChild(e);
}

function defaultInitPage() {
  closeAlert('#errorMessages');
  closeAlert('#successMessages') ;


}