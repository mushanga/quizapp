
function showAnswer(selector){

	$(selector+' input').prop('disabled', true);
	$(selector+' .correct').css("background-image","-webkit-linear-gradient( #fff,rgb(40, 180, 118))");
	$(selector+' .wrong').css("background-image","-webkit-linear-gradient( #fff,rgb(231, 111, 111) )");
	
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

