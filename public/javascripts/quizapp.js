
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

