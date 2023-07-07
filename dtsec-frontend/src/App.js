import './App.css';
import { useState } from "react";
import axios from 'axios';


function App() {

  const parseCSRFile = (event) => {
    console.log(event)

    const file = event.target.files[0];
    console.log(file)
    const reader = new FileReader();

    reader.onload = (e) => {
      const content = e.target.result;
      setFileContent(content);
    };

    reader.readAsText(file);
  };

  function parseCSRText() {
    const postCsr = {
      csr: fileContent
    }
    axios.post('http://localhost:8080/api/csr', postCsr, {
      headers: {
        'Content-Type': 'application/json'
      }
    })
      .then(function (response) {
          console.log('Antwort erhalten:', response.data);
          setData(response.data)
      })
      .catch(function (error) {
          console.error('Fehler bei der Anfrage:', error);
      });
  }

  function handleContentChange(event) {
    setFileContent(event.target.value)
  }

  const [ fileContent, setFileContent ] = useState("")
  const [data, setData] = useState(null);

  return (
    <div className="App">
      <header className="App-header">
        <h1>DT-Sec Programmieraufgabe</h1>
        <h4>PKCS #10 CSR Parser</h4>
      </header>
      <div className="App-form-container">
        {/* Form für die Datei Eingabe */}
        <form>
          <label>Wähle ein CSR aus:</label>
          <input type="file" className='inputBox' onChange={parseCSRFile} />
          <input type="button" value="Datei parsen" onClick={parseCSRText} />
        </form>
        <hr />
        <form>
          <label>Kopiere den Inhalt in folgende Box:</label>
          <textarea className='inputBox' name="CSR content" cols="1" rows="5" onChange={handleContentChange} value={fileContent} placeholder='-----BEGIN CERTIFICATE REQUEST-----...'></textarea>
          <input type="button" value="Text parsen" onClick={parseCSRText} />
        </form>
      </div>
      {/* Hier steht das Ergebnis */}
      <div className='App-result-container'>
        <h1>Ergebnis:</h1>
        <b>CSR: </b>
        <p>{fileContent}</p>
        <div>
          {data && Object.keys(data).length === 0 ? (
            <b className="App-error-msg">Der String konnte nicht geparsed werden. Überprüfe, ob der String PEM valide ist</b>
          ) : <p></p>}
        </div>
        <div>
          {data ? (
            <div className='App-result-value-container'>
              <div className='App-result-values'>
                <p><b>Common Name: </b>{data.name}</p>
                <p><b>Organisation: </b>{data.orga}</p>
                <p><b>PKI Algorithmus: </b>{data.pki}</p>
                <p><b>Land: </b>{data.country}</p>
                <p><b>Staat: </b>{data.state !== "none" ? data.state : "-"}</p>
                <p><b>OU: </b>{data.ou !== "none" ? data.ou : "-"}</p>
                <p><b>E-Mail: </b>{data.email !== "none" ? data.email : "-"}</p>
              </div>
            </div>

          ) : (
            <p>Warte auf Daten...</p>
          )}
        </div>
      </div>
      <footer>vorgelegt von <strong>Florian Zasada</strong></footer>
    </div>
  );
}

export default App;
