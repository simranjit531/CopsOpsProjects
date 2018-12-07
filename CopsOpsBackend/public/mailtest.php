<?php
// the message
$msg = "First line of text\nSecond line of text";

// use wordwrap() if lines are longer than 70 characters
$msg = wordwrap($msg,70);

// send email
echo mail("karayat.kailash@gmail.com","My subject",$msg) ? "Email Sent" : "Not Sent";
