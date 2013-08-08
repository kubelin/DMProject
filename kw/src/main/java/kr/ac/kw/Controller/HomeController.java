package kr.ac.kw.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import javazoom.jl.player.Player;

import kr.ac.kw.Commons.SqlSessionFactoryManager;
import kr.ac.kw.Recommand.CalRecommandation;
import kr.ac.kw.Service.FavoriteSongService;
import kr.ac.kw.Service.Mp3Service;
import kr.ac.kw.Service.SurveyService;
import kr.ac.kw.Service.UserService;
import kr.ac.kw.Vo.FavoriteSongVo;
import kr.ac.kw.Vo.Mp3Vo;
import kr.ac.kw.Vo.UserVo;
import kr.ac.kw.form.FavoriteSongForm;
import kr.ac.kw.form.Mp3Form;
import kr.ac.kw.form.SurveyForm;
import kr.ac.kw.form.UserForm;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sun.org.apache.xpath.internal.axes.HasPositionalPredChecker;

import beatit.BPM2SampleProcessor;
import beatit.EnergyOutputAudioDevice;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {
	
	@Inject
	@Named("userService")
	UserService userService;
	
	@Inject
	@Named("mp3Service")
	Mp3Service mp3Service;
	
	@Inject
	@Named("favoriteSongService")
	FavoriteSongService favoriteService;
	
	@Inject
	@Named("surveyService")
	SurveyService surveyService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * 	 < LOGIN PAGE >
	 * 	
	 * 	1. check whether user login or not ( session check ) 
	 * 	2. if not  display login page to user 
	 * 	3. else sendRedirect to First Page 
	 * @param locale
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index" )
	public String home(Locale locale, Model model, HttpSession session, HttpServletRequest request) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		System.out.println(request.getRequestURL());
		System.out.println(request.getRequestURI());
		System.out.println(request.getQueryString());
		
		
		return "login";
	}
	
/*	@RequestMapping(value ="main", method=RequestMethod.GET )
	public String mainPage(){
		
		return null;
	}*/

	/**
	 * 		<MAIN PAGE>
	 * @param model
	 * @param session
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value ="main", method=RequestMethod.GET )
	public String mainPage(ModelMap model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		// Session 만료시 재생성
//		System.out.println(session.isNew());
		if( session.isNew() ){ 
			System.out.println(session.isNew());
			 return "../index";  
			}
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
		int pageNo = 0;
		int totalPage = 0;
		int beginPageNo = 0;
		int cB = 0;
		int maxPage = 0;
		String genre = null;
		UserForm userForm = null;
		UserVo userVo = null;
		ArrayList<Mp3Vo> mp3List = new ArrayList<Mp3Vo>();
		ArrayList<FavoriteSongVo> fsList = new ArrayList<FavoriteSongVo>();
		HashMap<String, String> valueMap = new HashMap<String, String>();
		// 사용자 편의를 위한 추천 counting값 
		// 보낼 count값
		ArrayList<Integer> countArray = new ArrayList<Integer>();
		// 추천된 Count를 위한 Map 생성
		// main에서 돌리는 것보다 더 안정적
		HashMap<String, String> countMap = new HashMap<String, String>();
		
		
		// 페이징을 위한 변수 셋팅
		cB = request.getParameter("cB") == null ? 0 : Integer.parseInt(request.getParameter("cB"));
		pageNo = request.getParameter("pageNo") == null ? 1 : Integer.parseInt(request.getParameter("pageNo"));
		genre = request.getParameter("genre") == null ? "Ballad" :  request.getParameter("genre") ;
		
		
		valueMap.put("genre", genre);
		valueMap.put("page", Integer.toString(pageNo));
		
		try{
			
			userForm = (UserForm) session.getAttribute("userInfo");
			mp3List =  mp3Service.selectMp3List(sqlSession, valueMap); 
			fsList = favoriteService.selectFSList(sqlSession, userForm.getId());
			mp3List = mp3Service.setRecValue(mp3List, fsList);
			maxPage = mp3Service.selectTotalCount(sqlSession, genre);
			userVo = userService.selectUser(sqlSession, userForm.getId());
			
			// count 얻어오기 
			valueMap.put("id", userForm.getId());
			valueMap.put("genre", "Ballad");
			countArray.add(0, favoriteService.selectCount(sqlSession, valueMap)); 
			valueMap.put("genre", "Dance");
			countArray.add(1, favoriteService.selectCount(sqlSession, valueMap)); 
			valueMap.put("genre", "Rock");
			countArray.add(2, favoriteService.selectCount(sqlSession, valueMap)); 
			valueMap.put("genre", "Rap");
			countArray.add(3, favoriteService.selectCount(sqlSession, valueMap)); 
			
		}catch(NullPointerException e){ e.printStackTrace(); return "../index"; }
		catch(Exception e){e.printStackTrace();}finally{sqlSession.close();}
		
		System.out.println("==============");
		System.out.println(mp3List);
		System.out.println(pageNo);
		System.out.println(genre);
		System.out.println(userForm);
		System.out.println(userVo);
		
		if( pageNo >= 11 ){
			beginPageNo = ((pageNo / 10) * 10 ) + 1;
			totalPage =  (cB  + 1 ) * 10 ;
			//cB++;
			if( maxPage < totalPage){
				totalPage = maxPage;
			}
		}else{
			beginPageNo = 1;
			totalPage = maxPage > 10 ? 10 :  maxPage ;
		}
		System.out.println(totalPage);
		model.addAttribute("cB", cB);
		model.addAttribute("beginPageNo", beginPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("genre", genre);
		model.addAttribute("mp3List", mp3List);
		model.addAttribute("isSurvey", userVo.getIssurvey());
		model.addAttribute("countList", countArray);
		//mav.addObject("countList", countArray);
		
		return "main";
	}
	
	/** 
	 * < recommandMp3 >
	 * 1. session check
	 * 2. user's DB count check ( if count is over 10, create error ) 
	 * 3. insert recommandMp3
	 * 4. increase Mp3 count
	 * 5. get each genre's count
	 * @param model
	 * @param session
	 * @param response
	 * @param songNumber
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="recommandMp3")
	public ModelAndView recommandMp3(ModelMap model, HttpSession session, HttpServletResponse response, @RequestParam("songNumber")int songNumber, 
																																						 	@RequestParam("tabGenre")String Genre) throws Exception{
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
//		Writer writer = response.getWriter();
		ModelAndView mav = new ModelAndView("jsonReport");
		// Form 생성
		UserForm userForm = (UserForm) session.getAttribute("userInfo");
		FavoriteSongForm fsForm = new FavoriteSongForm();
		fsForm.setId(userForm.getId());
		fsForm.setFs_1(songNumber);
		fsForm.setGenre(Genre);
		// 보낼 count값
		ArrayList<Integer> countArray = new ArrayList<Integer>();
		// 추천된 Count를 위한 Map 생성
		// main에서 돌리는 것보다 더 안정적
		HashMap<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("id", userForm.getId());
		valueMap.put("genre", Genre);
		int changeCount = 0;
		
		if( userForm == null ){
			mav.addObject("state", "fail");
			return mav;
		}else{
			// validation 필요. ( 무결성 )
			try{
				
				changeCount = favoriteService.selectCount(sqlSession, valueMap);
				if( changeCount >= 10 ){
					mav.addObject("genre", Genre);
					mav.addObject("state", "over");
					return mav;
				}			
				
				favoriteService.insertFS(sqlSession, fsForm);
				mp3Service.updateCount(sqlSession,  Integer.toString(songNumber));

				mav.addObject("genre", Genre);
				mav.addObject("count", ++changeCount);
				
			}catch(Exception e){e.printStackTrace();}finally{sqlSession.close();}
		}
		
		return mav;
	}
	
/**
 *   	< Check User >
 * 		
 *  		1. 등록된 사용자 인지 Check 
 *   		2. if existed user then calling the redirect otherwise add new user.
 * @param model
 * @param session
 * @param response
 * @param id
 * @param name
 * @param gender
 * @param birthday
 * @throws IOException
 */
	@RequestMapping(value = "checkUser")
	public void checkUser(ModelMap model, HttpSession session, HttpServletResponse response,@RequestParam("id")String id,
																																					@RequestParam("name")String name,
																																					@RequestParam("gender")String gender,
																																					@RequestParam("birthday")String birthday
																																					) throws IOException{
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
		Writer writer =  response.getWriter();
		
		UserForm userForm = new UserForm();
		userForm.setId(id);
		userForm.setName(name);
		userForm.setGender(gender);
		userForm.setAge(Integer.parseInt(birthday.split("/")[2]));
		
		System.out.println(userForm);
		try{
			if( userService.searchUser(sqlSession, id) ){
				// 유저 세션 scope에 저장
				writer.write("success");
				session.setAttribute("userInfo", userForm);
				
			}else{
				writer.write("fail");
				session.setAttribute("userInfo", userForm);
				userService.insertUser(sqlSession, userForm);
			}
		}catch(Exception e){e.printStackTrace();}
		finally{
			sqlSession.close();
		}
		//return "index";
	}
	
	/**
	 * mp3 추천 받기 version 1
	 * @param mldel
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "recommandMp3v1")
	public ModelAndView recommandMp3v1(Model mldel, HttpSession session) throws Exception{
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
		UserForm userForm = (UserForm) session.getAttribute("userInfo");
		CalRecommandation calRecommand = new CalRecommandation();
		ArrayList<Mp3Vo> recommandedBalladList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedDanceList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedRockList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedRapList = new ArrayList<Mp3Vo>();
		
		ArrayList<UserVo> userList = new ArrayList<UserVo>();
		ArrayList<FavoriteSongVo> fsList = new ArrayList<FavoriteSongVo>();
		ArrayList<Mp3Vo> resultList = new ArrayList<Mp3Vo>();
		
		ModelAndView mav = new ModelAndView("jsonReport");
		
		try{
		userList = userService.searchAllUser(sqlSession);
		fsList = favoriteService.selectFSList(sqlSession, userForm.getId());
		
		if(calRecommand.countValidation(fsList).equals("fail")){
			mav.addObject("state", "fail");
			return mav;
		}
		
		resultList = mp3Service.selectRecommandedMp3v1(sqlSession, userList, fsList, userForm.getId() );
		
		for (Mp3Vo mp3Vo : resultList) {
			if( mp3Vo.getGenre().equals("Ballad") ){
				recommandedBalladList.add(mp3Vo);
			}else if( mp3Vo.getGenre().equals("Dance") ){
				recommandedDanceList.add(mp3Vo);
			}else if( mp3Vo.getGenre().equals("Rock") ){
				recommandedRockList.add(mp3Vo);
			}else if( mp3Vo.getGenre().equals("Rap / Hip-hop") ){
				recommandedRapList.add(mp3Vo);
			}
		}
		}catch(Exception e){e.printStackTrace();}finally{ sqlSession.close(); }
		
		mav.addObject("recBalladList", recommandedBalladList);
		mav.addObject("recDanceList", recommandedDanceList);
		mav.addObject("recRockList", recommandedRockList);
		mav.addObject("recRapList", recommandedRapList);
		
		
		System.out.println(recommandedBalladList);
		System.out.println(recommandedDanceList);
		System.out.println(recommandedRockList);
		System.out.println(recommandedRapList);
		
		return mav;
		
	}
	
	
	/**
	 * mp3 추천 받기 version 2
	 * @param model
	 * @param session
	 * @throws Exception
	 */
	@RequestMapping(value = "recommandMp3v2")
	public ModelAndView recommandMp3v2(Model model, HttpSession session) throws Exception{
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
		Map<String, ArrayList<Mp3Vo>> mp3Map = new HashMap<String, ArrayList<Mp3Vo>>();
		UserForm userForm = (UserForm) session.getAttribute("userInfo");
		ArrayList<FavoriteSongVo> fsList = new ArrayList<FavoriteSongVo>();
		CalRecommandation calRecommand = new CalRecommandation();
		ArrayList<Mp3Vo> recommandedBalladList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedDanceList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedRockList = new ArrayList<Mp3Vo>();
		ArrayList<Mp3Vo> recommandedRapList = new ArrayList<Mp3Vo>();
		
		ModelAndView mav = new ModelAndView("jsonReport");
		fsList = favoriteService.selectFSList(sqlSession, userForm.getId());
		
		if(calRecommand.countValidation(fsList).equals("fail")){
			mav.addObject("state", "fail");
			return mav;
		}
		
		try{
			mp3Map  = mp3Service.selectMp3RecommandedList(sqlSession, userForm.getId());
			fsList = favoriteService.selectFSList(sqlSession, userForm.getId());
			
			if( mp3Map.get("Ballad").size() >= 5 ){
				recommandedBalladList  = mp3Service.selectRecommandedMp3v2(sqlSession, mp3Map.get("Ballad"), fsList);
			}
			if( mp3Map.get("Dance").size() >= 5 ){
				recommandedDanceList = mp3Service.selectRecommandedMp3v2(sqlSession, mp3Map.get("Dance"), fsList);
			}
			if( mp3Map.get("Rock").size() >= 5 )
				recommandedRockList = mp3Service.selectRecommandedMp3v2(sqlSession, mp3Map.get("Rock"), fsList);
			if( mp3Map.get("Rap").size() >= 5 )
				recommandedRapList = mp3Service.selectRecommandedMp3v2(sqlSession, mp3Map.get("Rap"), fsList);
			/*for (Mp3Vo mp3Vo : mp3List) {
				System.out.println(mp3Vo);
			}*/
		}catch(Exception e){e.printStackTrace();}finally{ sqlSession.close();}
		
		
		mav.addObject("recBalladList", recommandedBalladList);
		mav.addObject("recDanceList", recommandedDanceList);
		mav.addObject("recRockList", recommandedRockList);
		mav.addObject("recRapList", recommandedRapList);
		
		
		System.out.println(recommandedBalladList);
		System.out.println(recommandedDanceList);
		System.out.println(recommandedRockList);
		System.out.println(recommandedRapList);
		return mav;
	}
	
	/**
	 * < Mp3 Auto Insert >
	 * @param session
	 * @throws Exception 
	 */
	@RequestMapping(value = "insertMp3List")
	public void insertMp3List(HttpSession session) throws Exception{
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
		String rootPath = "C:/mp3";
		File[] files = new File(rootPath).listFiles();
		File tempFile = null;
		Mp3Form mp3Form = new Mp3Form(); 
		System.out.println("======MP3 INSERT==========");
		System.out.println("총 = " + files.length);
		System.out.println("======MP3 INSERT==========");
		try{
		for( int i = 0 ; i < files.length ; i++){
				tempFile = files[i];
				//&& tempFile.getName().split(".")[1].equals("mp3")
				if ( tempFile.isFile() && tempFile.getName().substring(tempFile.getName().lastIndexOf(".")+1, tempFile.getName().length()).equals("mp3") ){
					System.out.println("Mp3파일입니다.");
					System.out.println(tempFile.getName());
					
					mp3Form = mp3Service.setMp3Info(tempFile);
					mp3Service.insertMp3(sqlSession, mp3Form);
					
				}else if( tempFile.isDirectory()){
					System.out.println("디렉토리 입니다.");
				}else{
					System.out.println("파일이 아닙니다");
					break;
				}
			}
		}catch(Exception e){e.printStackTrace();}finally{ sqlSession.close(); };
	}
	
	/**
	 * 설문 등록
	 * @param session
	 * @param v1
	 * @param v2
	 * @param etc
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "addSurvey")
	public String addSurvey(HttpSession session, @RequestParam("recommand_v1")int v1 , 
																		@RequestParam("recommand_v2")int v2 ,
																		@RequestParam("etc")String etc , HttpServletRequest request) throws Exception{
		
		SqlSession sqlSession = SqlSessionFactoryManager.getSqlSession(true);
		SurveyForm surveyForm = new SurveyForm();
		String resultEtc = URLDecoder.decode(URLEncoder.encode(etc, "ISO8859-1"), "UTF-8");

		UserForm userForm = (UserForm)session.getAttribute("userInfo");
		
		/*String[] encodings = new String[] {"EUC-KR", "UTF-8", "ISO8859-1"};
		
		for (String encoding1 : encodings) {
		    String encoded = URLEncoder.encode(etc, encoding1);
		    System.out.println(encoded);
		    System.out.print("\t"+encoding1);
		      
		    for (String encoding2 : encodings) {
		        String decoded = URLDecoder.decode(encoded, encoding2);
		        System.out.print(decoded + "\t\t");
		    }
		    System.out.println("\n");
		}
		*/
		surveyForm.setId(userForm.getId());
		surveyForm.setRecommand_v1(v1);
		surveyForm.setRecommand_v2(v2);
		surveyForm.setEtc(resultEtc);
		
		try{
			
			surveyService.insertSurvey(sqlSession, surveyForm);
			userService.updateIsServey(sqlSession, userForm.getId());
			
		}catch(Exception e){}finally{ sqlSession.close(); }
		
		return "bye";
	}
	
	@RequestMapping(value= "bye")
	public String bye() throws IOException, UnsupportedAudioFileException{
		return "bye";
	}
	
}
