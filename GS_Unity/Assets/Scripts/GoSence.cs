using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GoSence : MonoBehaviour {
	public void Click(string input){
		Debug.Log("Go:"+input);
		SceneManager.LoadScene(input);
	}
}
