var loadedImagesCounter;

var gradImg1,gradImg2;

var img1,img2,img3,img4;

var pimg1,pimg2,pimg3,pimg4;

var width1=540;
var height1=960;
var canvas1;

var width2=1200;
var height2=1920;
var canvas2;

var txtFnts=['sans-serif','days.woff','nunito.woff','el_messiri.woff','rubik_he.woff','hind_siliguri.woff','rubik_cy.woff','roboto.woff','poppins.woff','noto_ge.woff','kodchasan.woff','noto_ar.woff','noto_am.woff','nunito.woff'];

var txtFnt="cstfnttxt144";

var fontsDir="./fonts/";

var select,button;

var locals=['en','ru','uk','be','ka','az','hy','kk','uz','de',
				'fr','it','es','pt','pl','bg','sr','el','hu','da',
				'is','lv','lt','et','nb','nl','ro','sq','sk','sl',
				'fi','tr','hr','cs','sv','ms','in','id','iw','he',
				'ca','af','ar','am','th','hi','bn','ja','ko','vi',
				'mk','bs','mn'];
				
var lIndex;
var lCount;
var isMakeAll=false;

var locales={
	"en": {
		"font": 1,
		"fontSize": 64,
		"t1": "Experimental",
		"t1ext": "Experiment\r\nalways will be\r\nopening\r\nNEO!",
		"t2": "IDM",
		"t2ext": "In this case\r\nthe MUSIC\r\nconcept\r\nis a variable",
		"t3": "Ambient",
		"t3ext": "Boundless\r\ntotality\r\nof expression\r\nof the moment\r\nof true\r\nof life",
		"t4": "Psychedelic",
		"t4ext": "Possibility of\r\ncreative experiment\r\nto express\r\nto a side of\r\na unique\r\ncrystal\r\nof the actor",
		"wFontSize": 112,
		"wFontSize2": 72
	},
	"ru": {
		"font": 1,
		"fontSize": 64,
		"t1": "Экcпериментал",
		"t1ext": "Эксперимент\r\nвсегда будет\r\nоткрытием\r\nНОВОГО!",
		"t2": "IDM",
		"t2ext": "В данном случае\r\nконцепция\r\nМУЗЫКИ\r\nявляется\r\nпеременной\r\nвеличиной",
		"t3": "Эмбиент",
		"t3ext": "Беспредельная\r\nтотальность\r\nвыражения\r\nмомента\r\nистины\r\nбытия",
		"t4": "Психоделика",
		"t4ext": "Возможность\r\nтворческого\r\nэксперимента -\r\n выражать грани\r\nуникального\r\nкристалла\r\nартиста",
		"wFontSize": 96,
		"wFontSize2": 72
	},
	"uk": {
		"font": 1,
		"fontSize": 64,
		"t1": "Експериментал",
		"t1ext": "Експеримент\r\nзавжди буде\r\nвідкриттям\r\nНОВОГО!",
		"t2": "IDM",
		"t2ext": "В даному випадку\r\nконцепція\r\nМУЗИКИ\r\nє\r\nзмінною\r\nвеличиною",
		"t3": "Ембіент",
		"t3ext": "Безмежна\r\nтотальність\r\nвираження\r\nмоменту\r\nістини\r\nбуття",
		"t4": "Психоделіка",
		"t4ext": "Можливість\r\nтворчого\r\nексперименту -\r\nвиражати грані\r\nунікального\r\nкристала\r\nартиста",
		"wFontSize": 96,
		"wFontSize2": 72
	}
}

function main()
{	
	var isPro=true;
	
	var loc=localStorage["sel_locale"];
	if(!loc) {
		loc='ru';
	}	
	
	initCanvas();
	
	loadedImagesCounter=0;
	
	gradImg1=createImage("./img/phone/grad1_clouds_new.png");
	gradImg2=createImage("./img/planshet7/grad2_clouds_new.png");
	
	
	img1=createImage("./img/phone/framed/1_"+loc+".png");
	img2=createImage("./img/phone/framed/2_"+loc+".png");
	img3=createImage("./img/phone/framed/3_"+loc+".png");
	img4=createImage("./img/phone/framed/4_"+loc+".png");
	
	pimg1=createImage("./img/planshet7/framed/1_"+loc+".png");
	pimg2=createImage("./img/planshet7/framed/2_"+loc+".png");
	pimg3=createImage("./img/planshet7/framed/3_"+loc+".png");
	pimg4=createImage("./img/planshet7/framed/4_"+loc+".png");
}

function imageOnload()
{
	loadedImagesCounter++;
	if(loadedImagesCounter>=10) {
		select=createSelect();
		createNbsp();
		button=createButton();
		
		var selLocale=localStorage["sel_locale"];
	
		if(selLocale!='-') {
			if(!selLocale) {
				selLocale='ru';
			}	
			select.value=selLocale;
			select.dispatchEvent(new Event('change'));
		}
	}
}


function makeSS(loci)
{
	var loca=locales[loci];
	if(loca) {
		document.getElementById("ss").innerHTML="Please wait...";
		var fnti=loca.font;
		var fontSize=loca.fontSize;	
		setTimeout(function() {
			window.requestAnimationFrame(function() {
				loadFontAndExecFn(txtFnt,txtFnts,fnti,function() {
					document.getElementById("ss").innerHTML="";
					
					var locale=select.value;
				
					start1(locale,1,img1,loca.t1,fnti,fontSize,loca.t1ext,loca.wFontSize2);
					start1(locale,2,img2,loca.t2,fnti,fontSize,loca.t2ext,loca.wFontSize2);
					start1(locale,3,img3,loca.t3,fnti,fontSize,loca.t3ext,loca.wFontSize2);
					start1(locale,4,img4,loca.t4,fnti,fontSize,loca.t4ext,loca.wFontSize2);
					//start1(locale,4,img4,loca.t4,fnti,fontSize,loca.tw,loca.wFontSize);
					
					
					document.getElementById("ss").appendChild(document.createElement("br"));
					
					start2(locale,1,pimg1,loca.t1,fnti,fontSize,loca.t1ext,loca.wFontSize2);
					start2(locale,2,pimg2,loca.t2,fnti,fontSize,loca.t2ext,loca.wFontSize2);
					start2(locale,3,pimg3,loca.t3,fnti,fontSize,loca.t3ext,loca.wFontSize2);
					start2(locale,4,pimg4,loca.t4,fnti,fontSize,loca.t4ext,loca.wFontSize2);
					//start2(locale,4,pimg4,loca.t4,fnti,fontSize,loca.tw,loca.wFontSize);
					
					select.disabled=false;
					select.focus();
					
					if(isMakeAll) {
						if(lIndex<lCount-1) {
							lIndex++;
							select.value=locals[lIndex];
							select.dispatchEvent(new Event('change'));
						}
						else {
							isMakeAll=false;
							button.disabled=false;
						}
					}
				
				});	
			});
		},100);
	}
	else {
		document.getElementById("ss").innerHTML="";
	}
}

function createNbsp()
{
	var nbsp=document.createElement("span");
	nbsp.innerHTML="&nbsp;";
	document.getElementById("toolbar").appendChild(nbsp);
	
}

function createButton()
{
	var button = document.createElement("button");
	button.innerHTML='Make all';
	button.addEventListener("click", function() {
		button.disabled=true;
		lIndex=0;
		lCount=locals.length;
		isMakeAll=true;
		select.value=locals[lIndex];
		select.dispatchEvent(new Event('change'));
	});
	
	//document.getElementById("toolbar").appendChild(button);
	
	return button;
}

function saveImage(imgData,type,locale,number)
{
	var image_base64 = imgData.replace(/^data:image\/png;base64,/, "");
	
	var request = new XMLHttpRequest();
	var url = "image_save.php";
	
	var params = "imgdata="+image_base64+"&type="+type+"&locale="+locale+"&number="+number;
	
	request.open("POST", url, true);
	
	request.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	
	request.addEventListener("readystatechange", function () {
		if(request.readyState === 4 && request.status === 200) {       
		}
	});
	
	request.send(params);
}


function createSelect()
{ 
    var select = document.createElement("select");
    select.name = "locale";
    select.id = "locale"
	
	var option = document.createElement("option");
	
	option.value = "-";
    option.text = " ";
    select.appendChild(option);

    for (var val in locales)
    {
        option = document.createElement("option");
        option.value = val;
        option.text = val.charAt(0).toUpperCase() + val.slice(1);
        select.appendChild(option);
    }
 
    var label = document.createElement("label");
    label.innerHTML = "Choose locale: "
	label.style.fontWeight='bold';
    label.htmlFor = "locale";
	
	select.addEventListener("change", function() {
		localStorage['sel_locale']=select.value;
		if(select.value != "-") {
			select.disabled=true;
			makeSS(select.value);
		}
	});
 
    document.getElementById("toolbar").appendChild(label).appendChild(select);
	
	select.focus();
	return select;
}
	

function createImage(src)
{
	var img=new Image();
	img.onload=imageOnload;
	img.src=src;
	return img;
}

function initCanvas()
{
    canvas1 = document.createElement('canvas');
    canvas1.setAttribute('width', width1);
    canvas1.setAttribute('height', height1);
	
	canvas2 = document.createElement('canvas');
    canvas2.setAttribute('width', width2);
    canvas2.setAttribute('height', height2);
}

function loadFontAndExecFn(name, fnta, fnti, fn) {
	if(fnti)
	{
		var path=fontsDir+fnta[fnti];
		var newFont = new FontFace(name, 'url('+path+')');
		newFont.load().then(function (loaded) {
		  document.fonts.add(loaded);
		  fn();
	  }).catch(function (error) {
		  fn();
	  });
	}
	else {
		fn();
	}
}


function fillTextMultiLine(ctx, text, x, y) {
  var lineHeight = ctx.measureText("M").width * 1.2;
  var lines = text.split("\n");
  for (var i = 0; i < lines.length; ++i) {
    ctx.fillText(lines[i], x, y);
    y += lineHeight;
  }
}

function start1(locale,number,img,txt,fnti,fontSize,txt2=false,txt2FontSize=0)
{	
	var canvas=canvas1;
	var width=width1;
	var height=height1;
	var ctx=canvas.getContext("2d");
	
	var imgScale=0.9;
	
	var fntScale=1;
	var fntScale2=0.6;
	
	fontSize=Math.floor(fontSize*fntScale);
	
	if(txt2FontSize) {
		txt2FontSize=Math.floor(txt2FontSize*fntScale2);
	}
		
	
	/*
	var gradient=ctx.createLinearGradient(0,0,0,height);
	gradient.addColorStop(0,"#eeffff");
	gradient.addColorStop(1,"#556666");
	*/
	
	/*
	var gradient = new DitheredLinearGradient(0,0,0,height);		
	gradient.addColorStop(0,parseInt('0xee',16),parseInt('0xff',16),parseInt('0xff',16));
	gradient.addColorStop(1,parseInt('0x55',16),parseInt('0x66',16),parseInt('0x66',16));
	gradient.fillRect(ctx,0,0,width,height);
	*/
	
	//var pattern=ctx.createPattern(bgImg,"repeat");
	ctx.fillStyle="#fff";
	//ctx.fillStyle=gradient;
	//ctx.fillStyle=pattern;
	ctx.fillRect(0,0,width,height);
	
	ctx.drawImage(gradImg1,0,0);
		
	var imgWidth=img.width*imgScale;
	var imgHeight=img.height*imgScale;
	
	var imgX=(width-imgWidth)/2;
	var imgY=height-imgHeight;
	
	ctx.drawImage(img,imgX,imgY,imgWidth,imgHeight); 
	
	
	var txtFntName;
	var bold;
	
	if(fnti) {
		txtFntName=txtFnt;
		bold="";
	}
	else {
		txtFntName=txtFnts[0];
		bold="bold "; 
	}
	
	ctx.font = bold+fontSize+"px "+txtFntName;
	ctx.textAlign="center";
	ctx.textBaseline = "top";
	ctx.fillStyle = "#FFCC33";
	
	var textX=Math.floor(width/2);
	var textY=40;
	ctx.fillText(txt, textX, textY, width-52);
	
	
	/*
	if(txt2) {
		var text2X=textX;
		var text2Y=Math.floor(height-imgHeight/2);
		ctx.font = bold+txt2FontSize+"px "+txtFntName;
		
		ctx.strokeStyle = "#edeff3";
		ctx.lineWidth = 8;		
		ctx.shadowBlur = 0;
		ctx.strokeText(txt2, text2X, text2Y, width);
		
		ctx.strokeStyle = "#008000";
		ctx.lineWidth = 4;
		ctx.shadowColor = '#000';
		ctx.shadowOffsetX=0;
		ctx.shadowOffsetY=0;
		ctx.shadowBlur = 2;
		ctx.strokeText(txt2, text2X, text2Y, width);
		
		ctx.fillStyle = "#32CD32";
		ctx.shadowBlur = 0;
		ctx.fillText(txt2, text2X, text2Y, width);
	}
	*/
	
	if(txt2) {
		
		ctx.font = bold+txt2FontSize+"px "+txtFntName;
		var lineHeight = ctx.measureText("M").width * 1.2;
		
		var lines = txt2.split("\n");
		
		var text2X=textX;
		var text2Y=Math.floor(height-lineHeight*(lines.length+1)); //Math.floor(height-imgHeight/2);
		
	
		ctx.strokeStyle = "#444";
		ctx.lineWidth=4;
		ctx.fillStyle = "#000";
		ctx.beginPath();
		ctx.roundRect(16, text2Y-lineHeight+16, width-32, lineHeight*(lines.length+2)-32, 16);
		
		
		ctx.shadowColor = '#000';
		ctx.shadowOffsetX=0;
		ctx.shadowOffsetY=0;
		ctx.shadowBlur = 16;
		
		ctx.stroke();
		ctx.fill();

		
		
		
		
		/*
		ctx.strokeStyle = "#edeff3";
		ctx.lineWidth = 8;		
		ctx.shadowBlur = 0;
		ctx.strokeText(txt2, text2X, text2Y, width);
		
		ctx.strokeStyle = "#008000";
		ctx.lineWidth = 4;
		ctx.shadowColor = '#000';
		ctx.shadowOffsetX=0;
		ctx.shadowOffsetY=0;
		ctx.shadowBlur = 2;
		ctx.strokeText(txt2, text2X, text2Y, width);
		*/
		
		//ctx.fillStyle = "#32CD32";
		
		ctx.fillStyle = "#FF9900";
		
		ctx.shadowBlur = 0;
		
		fillTextMultiLine(ctx,txt2, text2X, text2Y);
		
		//ctx.fillText(txt2, text2X, text2Y, width);
		
		
	}
	
	var imgData=canvas.toDataURL();
	saveImage(imgData,1,locale,number);
	
	var img2=new Image();
	img2.src = imgData;
	img2.style.width="auto";
	img2.style.height="75%";
	img2.style.marginRight="16px";
	img2.style.marginBottom="16px";
	
	document.getElementById("ss").appendChild(img2);
}



function start2(locale,number,img,txt,fnti,fontSize,txt2=false,txt2FontSize=0)
{	
	var canvas=canvas2;
	var width=width2;
	var height=height2;
	var ctx=canvas.getContext("2d");
	
	var imgScale=0.9;
	
	var fntScale=2.2222;
	var fntScale2=1.2255;
	
	fontSize=Math.floor(fontSize*fntScale);
	
	if(txt2FontSize) {
		txt2FontSize=Math.floor(txt2FontSize*fntScale2);
	}
		
	/*
	var gradient=ctx.createLinearGradient(0,0,0,height);
	gradient.addColorStop(0,"#eeffff");
	gradient.addColorStop(1,"#556666");
	*/

	/*
	var gradient = new DitheredLinearGradient(0,0,0,height);		
	gradient.addColorStop(0,parseInt('0xee',16),parseInt('0xff',16),parseInt('0xff',16));
	gradient.addColorStop(1,parseInt('0x55',16),parseInt('0x66',16),parseInt('0x66',16));
	gradient.fillRect(ctx,0,0,width,height);
	*/
	
	//var pattern=ctx.createPattern(bgImg,"repeat");
	ctx.fillStyle="#fff";
	//ctx.fillStyle=gradient;
	//ctx.fillStyle=pattern;
	ctx.fillRect(0,0,width,height);
	
	ctx.drawImage(gradImg2,0,0);
		
	var imgWidth=img.width*imgScale;
	var imgHeight=img.height*imgScale;
	
	var imgX=(width-imgWidth)/2;
	var imgY=height-imgHeight;
	
	ctx.drawImage(img,imgX,imgY,imgWidth,imgHeight); 
	
	
	var txtFntName;
	var bold;
	
	if(fnti) {
		txtFntName=txtFnt;
		bold="";
	}
	else {
		txtFntName=txtFnts[0];
		bold="bold "; 
	}
	
	ctx.font = bold+fontSize+"px "+txtFntName;
	ctx.textAlign="center";
	ctx.textBaseline = "top";
	ctx.fillStyle = "#FFCC33";
	
	var textX=Math.floor(width/2);
	var textY=100;
	ctx.fillText(txt, textX, textY, width-138);
	
	
	/*
	if(txt2) {
		var text2X=textX;
		var text2Y=Math.floor(height-imgHeight/2);
		ctx.font = bold+txt2FontSize+"px "+txtFntName;
		
		ctx.strokeStyle = "#edeff3";
		ctx.lineWidth = Math.floor(8*fntScale);
		ctx.shadowBlur = 0;		
		ctx.strokeText(txt2, text2X, text2Y, width);
		
		ctx.strokeStyle = "#008000";
		ctx.lineWidth = Math.floor(4*fntScale);
		ctx.shadowColor = '#000';
		ctx.shadowOffsetX=0;
		ctx.shadowOffsetY=0;
		ctx.shadowBlur = Math.floor(2*fntScale);
		ctx.strokeText(txt2, text2X, text2Y, width);
		
		ctx.fillStyle = "#32CD32";
		ctx.shadowBlur = 0;		
		ctx.fillText(txt2, text2X, text2Y, width);
	}
	*/
	
	if(txt2) {
		
		ctx.font = bold+txt2FontSize+"px "+txtFntName;
		var lineHeight = ctx.measureText("M").width * 1.2;
		
		var lines = txt2.split("\n");
		
		var text2X=textX;
		var text2Y=Math.floor(height-lineHeight*(lines.length+1)); //Math.floor(height-imgHeight/2);
		
	
		ctx.strokeStyle = "#444";
		ctx.lineWidth=4;
		ctx.fillStyle = "#000";
		ctx.beginPath();
		ctx.roundRect(16, text2Y-lineHeight+16, width-32, lineHeight*(lines.length+2)-32, 16);
		
		
		ctx.shadowColor = '#000';
		ctx.shadowOffsetX=0;
		ctx.shadowOffsetY=0;
		ctx.shadowBlur = 16;
		
		ctx.stroke();
		ctx.fill();

		
		
		
		
		/*
		ctx.strokeStyle = "#edeff3";
		ctx.lineWidth = 8;		
		ctx.shadowBlur = 0;
		ctx.strokeText(txt2, text2X, text2Y, width);
		
		ctx.strokeStyle = "#008000";
		ctx.lineWidth = 4;
		ctx.shadowColor = '#000';
		ctx.shadowOffsetX=0;
		ctx.shadowOffsetY=0;
		ctx.shadowBlur = 2;
		ctx.strokeText(txt2, text2X, text2Y, width);
		*/
		
		//ctx.fillStyle = "#32CD32";
		
		ctx.fillStyle = "#FF9900";
		
		ctx.shadowBlur = 0;
		
		fillTextMultiLine(ctx,txt2, text2X, text2Y);
		
		//ctx.fillText(txt2, text2X, text2Y, width);
		
		
	}

	var imgData=canvas.toDataURL();
	saveImage(imgData,2,locale,number);
	
	var img2=new Image();
	img2.src = imgData;
	img2.style.width="auto";
	img2.style.height="70%";
	img2.style.marginRight="16px";
	img2.style.marginBottom="16px";

	document.getElementById("ss").appendChild(img2);	
}

window.onload=function() {
	main();
}