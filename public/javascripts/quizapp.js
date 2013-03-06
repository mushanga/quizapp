
function showAnswer(selector){

	$(selector+' input').prop('disabled', true);
	
	paintAnswerLabels(selector);
	
	var selected = $(selector+" input:checked");
	var pt = 0;
	if (selected.length > 0){
		$(selector+" input:checked").each(function( index ) {
			var answerId = this.id.split('-')[1];
			var val = $('#fraction-'+answerId).val();
			var dVal = parseFloat(val);
			pt = pt + dVal;
		});
	}

	$(selector+ ' .result').html((pt/100).toFixed(2)+' / 1.00');
	$(selector+ ' .result').show();

}
function paintAnswerLabels(selector){
	
	var from = '#ffffff';
	var toCorrect = '#28B476';
	var toWrong = '#E76F6F';
	
	$(selector+' .correct').css("background-image","linear-gradient( "+from+","+toCorrect+")");
	$(selector+' .wrong').css("background-image","linear-gradient( "+from+","+toWrong+" )");
	//chrome
	$(selector+' .correct').css("background-image","-webkit-linear-gradient("+from+","+toCorrect+")");
	$(selector+' .wrong').css("background-image","-webkit-linear-gradient( "+from+","+toWrong+" )");
	//ff
	$(selector+' .correct').css("background-image","-moz-linear-gradient( "+from+","+toCorrect+")");
	$(selector+' .wrong').css("background-image","-moz-linear-gradient( "+from+","+toWrong+" )");
	//opera
	$(selector+' .correct').css("background-image","-o-linear-gradient( "+from+","+toCorrect+")");
	$(selector+' .wrong').css("background-image","-o-linear-gradient( "+from+","+toWrong+" )");
	//ie
	$(selector+' .correct').css("background-image","-ms-linear-gradient( "+from+","+toCorrect+")");
	$(selector+' .wrong').css("background-image","-ms-linear-gradient( "+from+","+toWrong+" )");
	//ie < 10
	$(selector+' .correct').css("filter","progid:DXImageTransform.Microsoft.gradient(startColorstr='"+from+"',endColorstr='"+toCorrect+"')");
	$(selector+' .wrong').css("filter","progid:DXImageTransform.Microsoft.gradient(startColorstr='"+from+"',endColorstr='"+toWrong+"')");
	//	Android < 4.0
	$(selector+' .correct').css("background-image","-webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, "+from+"),color-stop(100%, "+toCorrect+"))");
	$(selector+' .wrong').css("background-image","-webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, "+from+"),color-stop(100%, "+toWrong+"))");
	
}
function showAnswers(quizId){
	var count = 0;
	var selector = "#quiz-"+quizId;
	$(selector+" .question").each(function( index ) {
		showAnswer('#'+this.id);
		count++;
	});
	
	var result = 0;
	
	$(selector+' .result').each(function( index ) {
		var questionResult = $(this).html().split(" ")[0];
		result = result + parseFloat(questionResult);
	});
	
	$(selector+' .total-result').html(result.toFixed(2)+' / '+count.toFixed(2));
}

function getQuizById(quizId, callback, parameter){

	$.ajax({

		url : getQuizDataUrl(quizId),
		dataType : "json",
		success :  function(data)
		{
			callback(data,parameter);
			
		},
		error : function(er)
		{
		
		}

	});
}

function answer(questionId, answerId){	

	if(answers[questionId] == answerId){
		answers[questionId] = -1;
		return false;
	}else{
		
		answers[questionId] = answerId;


	   
	}
	return true;
}

