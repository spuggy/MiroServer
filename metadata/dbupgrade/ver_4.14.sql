CREATE TABLE mr.webpages
(
  id bigint NOT NULL,
  menucontextid integer NOT NULL,
  description character varying(255) NOT NULL,
  pagetext text NOT NULL,
  pagename character varying(50) NOT NULL,
  CONSTRAINT webpage_pkey PRIMARY KEY (id)
)
;
ALTER TABLE mr.webpages OWNER TO mobriz;


INSERT INTO mr.webpages (id, menucontextid, description, pagetext, pagename) VALUES (1, 4, 'Help Index Page', '<ul>

<li><a href="#1">How do I create a new survey?</a></li>

<li><a href="#2">What are the guidelines for creating a survey?</li>

<li><a href="#3">What answer types are available?</a></li>

<li><a href="#4">What is the maximum number of questions that can be put into a survey?</a></li>

<li><a href="#5">What is the maximum number of surveys that I can set up?</a></li>

<li><a href="#6">Can I override the sequence of completing questions?</a></li>

<li><a href="#7">What is What do ''Required'' and ''Sticky'' mean?</a></li>

<li><a href="#8">What is ''Short Name'' used for?</a></li>

<li><a href="#9">How do I set up new Web Site users?</a></li>

<li><a href="#10">How do I set up new Mobile Phone users?</a></li>

<li><a href="#11">Which Mobile Phones are compatible with Mobriz?</a></li>

<li><a href="#12">Which Mobile Network Operators are compatible with Mobriz?</a></li>

<li><a href="#13">Do the phones need to be set up in any special way?</a></li>

<li><a href="#14">How do I get a new survey on to the Mobile Phones of the agents?</a></li>

<li><a href="#15">What if I want to modify the survey after agents have started using it?</a></li>

<li><a href="#16">How do I see the results?</a></li>

</ul>





<h6><a name="1" >How do I create a new survey?</a></h6>

<p>Use the ''Create New Survey'' button on the ''Surveys'' page of the Mobriz web site.</p>



<h6><a name="2" >What are the guidelines for creating a survey?</a></h6>



<ul>

<li> Keep the questions brief.  Remember that the screen of a mobile phone is small. The system will allow a maximum of 100 characters per question.  For Single-Choice, Multiple-Choice and Percent questions keep the answers short.  The system allows 20 characters max.</li>

<li> Keying in text answers takes much longer than selecting from single or multiple choice questions.  Bear this in mind when designing a survey and keep the number of text questions to the minimum.</li>

<li> Start the survey with an introductory message.  It may be helpful to state the number of questions that need to be answered.</li>

<li> Finish the survey with a finishing message e.g. ''End of Survey ? Thanks''.  This will be a useful marker if you intend to use branching logic.</li>

</ul>





<h6><a name="3" >What answer types are available?</a></h6>

<ul>

<li><b>Text</b> &#045; lows user to type in a text answer in the same way they would type a text message.</li>

<li><b>Multi Choice</b> &#045; allows user to select one or more options from a list.</li>

<li><b>Single Choice</b> &#045; allows user to select one option from a list.</li>

<li><b>Percent</b> &#045; allows user to enter a percentage against one or more options.  All must add up to 100%.</li>

<li><b>Number</b> &#045; user can enter an integer (whole number) only.  Keyboard automatically switches to numeric mode (as if dialling a phone number).</li>

<li><b>Date</b> &#045; user can enter a date.</li>

<li><b>Decimal</b> &#045; user can enter a numeric value with decimal places.  Keyboard automatically switches to numeric mode.</li>

<li><b>Message</b> &#045; user is not required to enter anything.  Useful for start and finish messages.</li>

</ul>





<h6><a name="4" >What is the maximum number of questions that can be put into a survey?</a></h6>

The physical limit will be dictated by the amount of memory available on the phone, which will differ from handset to handset.  To be easy for users, however, it is a good idea to keep surveys below 100 questions.  Consider breaking very long surveys into separate ones for easy completion.



<h6><a name="5" >What is the maximum number of surveys that I can set up?</a></h6>

There is no limit to the number of surveys that can be created on the web site.  The number of surveys that can be stored on a handset is dictated by the amount of memory available on the phone, and will therefore depend on how long each survey is.  It is a good idea to keep the number of surveys available to any one user to below ten.



<h6><a name="6" >Can I override the sequence of completing questions?</a></h6>

Mobriz allows you to build in ''branching logic''.  If you choose not to use Branching Logic the user will progress through the questions in sequence until the end of the survey is reached.  With Branching Logic you can override the standard sequence in one of two ways:  You can add a link to a question to specify the next question to be answered, or for single-choice questions you can add links to the answer options to route to specific questions dependant on how they are answered.



<h6><a name="7" >What is What do ''Required'' and ''Sticky'' mean?</a></h6>

If you mark a question as ''Required'' the user must enter a response; if not they will be able to go to the next question without entering an answer.

If you mark a question as ''Sticky'' the answer that the user enters will be ''remembered'' when they complete subsequent surveys.  This could be useful, for example, if numerous surveys are to be undertaken in one place, and will avoid the user having to type in the place name for each survey.  Sticky answers can be overwritten by the user though.



<h6><a name="8" >What is ''Short Name'' used for?</a></h6>

''Short Name'' has two purposes:  It is used to display branching logic in the Survey screen, and it is used to populate column headings in the download response spreadsheet.



<h6><a name="9" >How do I set up new Web Site users?</a></h6>

Select the ''Admin'' page on the web site, and use the ''Add'' button to create a new user.



<h6><a name="10" >How do I set up new Mobile Phone users?</a></h6>

1 Select the ''Admin'' page on the web site, and use the ''Add'' button to create a new user.

2 Use the SMS button to send the new user a text message containing the link to download Mobriz

3 Advise the user of the pin number you have created for them.  They will need to enter this into the Admin screen on the phone, the first time they use Mobriz.



<h6><a name="11" >Which Mobile Phones are compatible with Mobriz?</a></h6>

Check the ''About'' document to see the latest list of compatible handsets.



<h6><a name="12" >Which Mobile Network Operators are compatible with Mobriz?</a></h6>

Mobriz will work with phones on O2, Vodafone, Orange and T-Mobile.  It will work on most virtual networks like Virgin.  Mobriz will NOT work with phones on the ''3'' network as  ''3'' does not allow access to most internet servers.



<h6><a name="13" >Do the phones need to be set up in any special way?</a></h6>

The handsets need to able to access the internet using GPRS or 3G.  If you can access a public web site from your phone it will work with Mobriz.



<h6><a name="14" >How do I get a new survey on to the Mobile Phones of the agents?</a></h6>

Use the ''Visibility'' field in the Survey Definition screen to select the users who need to get the survey.  Hold the Control key down whilst selecting multiple users.



<h6><a name="15" >What if I want to modify the survey after agents have started using it?</a></h6>

Mobriz allows you to modify the survey once results come in.  Users will need to use the ''Download Surveys'' button on their phones to get the latest version.  Bear in mind that if questions are added or removed it may have an affect on the analysis of the results.



<h6><a name="16" >How do I see the results?</a></h6>

Use the ''Responses'' page on the web site to see the results.  You can drill down into individual results or use the ''Download'' button to export all the results for a given survey into a spreadsheet on your PC for processing locally.



', 'help');

