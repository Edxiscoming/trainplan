
function resize(left,right,minleft,maxleft){
	//alert("hi");
    var WinW = $(window).width();
    var WinH = $(window).height();
    var RightId = '#'+right;
    var LeftId = '#'+left;
    var LeftMinW = minleft; 
    var LeftMaxW = maxleft; 
   
    var LeftboxW1 = $(LeftId).width();
    var RightboxW1 = $(RightId).width();
    
    var changerDiff1 = LeftboxW1 - LeftMinW;
    var changerDiff2 = changerDiff1+RightboxW1;

    var changerDiff3 = LeftboxW1 - LeftMaxW;
    var changerDiff4 = changerDiff3+RightboxW1;

//拖动左边时
    var PrimeW1 = LeftboxW1;

    function NewWLeft(){
        var LeftboxW1 = $(LeftId).width();
        if(LeftboxW1 == PrimeW1){

        }else{
            var diff = PrimeW1 - LeftboxW1;
            $(RightId).css("width", RightboxW1 + diff);
        }
    }
    $(LeftId).resizable({
            //grid: 50,
    	    minWidth: LeftMinW,
            maxWidth: LeftMaxW,
            maxHeight: WinH-100,  
            handles: 'e, s', 
            //edge:4,  
            onStartResize: function(e){
               // alert("左");
            },            
            onResize: function(e){
                NewWLeft();
            }
    });

//拖动右边时

    var PrimeW2 = RightboxW1;
    //alert(PrimeW2);


    function NewWRight(){
        var RightboxW2 = $(RightId).width();
        if(RightboxW2 == PrimeW2){

        }else{
            var diff2 = PrimeW2 - RightboxW2;
            $(LeftId).css("width", LeftboxW1 + diff2);
            $(RightId).css("left", 0);
        }
    }
    $(RightId).resizable({
            //grid: 50,
            maxWidth: changerDiff2,
            minWidth: changerDiff4,
            maxHeight: WinH-100,  
            handles: 's, w',  
            //edge:1,     

         /*   onStartResize: function(e){
                alert("右");
            },*/
            onResize: function(e){
                NewWRight();
            }
/*            onStopResize: function(e){
                alert();
            }*/
    });

//拖动下左边时

    
}