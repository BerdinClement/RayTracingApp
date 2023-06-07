import {useState} from "react";

export default function Home() {

    const [isLoading, setIsLoading] = useState(false);
    const [isTraitement, setIsTraitement] = useState(false);
    const [result, setResult] = useState(null);
    const [error, setError] = useState(null);
    const [isFile, setIsFile] = useState(false);
    const [file, setFile] = useState(null);
    const [nbRays, setNbRays] = useState(1);
    const [nbThreads, setNbThreads] = useState(1);

    const handleFile = (e) => {
        e.preventDefault()
        setIsLoading(true)
        setFile(e.target.files[0]);
        if(e.target.files[0].name.split('.').pop() !== "txt") {
            setError("File is not a .txt file")
            setIsFile(false)
        } else {
            setIsFile(true)
            setError(null)
        }
        setIsLoading(false)
    }

    const reset = () => {
        setIsLoading(false)
        setError(null)
        setIsFile(false)
        setFile(null)
    }

    const handleUpload = async (e) => {
        e.preventDefault()
        setIsLoading(true)

        const formData = new FormData();
        formData.append('uploadedfile', file);

        const response = await fetch('http://localhost:5394/upload', {
            method: 'POST',
            body: formData,
        });

        const data = await response.json()

        if(data.status === "success") {
            setIsLoading(false)
            setError(null)
            setIsFile(false)
            setFile(null)
            setIsTraitement(true)

            const myHeaders = new Headers();
            myHeaders.append("Content-Type", "application/json");

            const raw = JSON.stringify({
                "nbRays": nbRays,
                "nbThreads": nbThreads
            });

            const requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: raw,
                redirect: 'follow'
            };

            const response = await fetch("http://localhost:5394/traitement", requestOptions)
            const data = await response.blob()

            const url = window.URL.createObjectURL(data)
            setResult(url)
            setIsTraitement(false)

        } else {
            setIsLoading(false)
            setError(data.message)
            setIsFile(false)
            setFile(null)
        }
    }

    return (
        <div className={"h-screen flex bg-gray-800"}>
            <div className={"flex justify-center items-center m-auto w-full h-full"}>
                <div className={"flex flex-col items-center w-10/12"}>

                    <h1 className={"text-4xl text-white font-bold"}>Welcome to the <span className={"text-blue-500"}>Ray</span> <span className={"text-red-500"}>Caster</span> App</h1>
                    <h2 className={"text-2xl text-white font-bold"}>Send a scene file</h2>
                    <h2 className={"text-md text-white italic"}>By BERDIN Clément / CARON Samuel</h2>

                    <form className={"flex flex-col items-center mt-4"}>
                        {
                            !isFile && !error && !isTraitement && !result && (
                                <div className="flex items-center justify-center w-[300px]" >
                                    <label htmlFor="dropzone-file"
                                           className="flex flex-col items-center justify-center w-full h-64 border-2 border-dashed rounded-lg cursor-pointer hover:bg-bray-800 bg-gray-700 border-gray-600 hover:border-gray-500 hover:bg-gray-600">
                                        <div className="flex flex-col items-center justify-center pt-5 pb-6">
                                            <svg aria-hidden="true" className="w-10 h-10 mb-3 text-gray-400" fill="none"
                                                 stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                                      d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
                                            </svg>
                                            <p className="mb-2 text-sm text-gray-500 dark:text-gray-400"><span
                                                className="font-semibold">Click to upload</span> or drag and drop</p>
                                            <p className="text-xs text-gray-500 dark:text-gray-400">TXT</p>
                                        </div>
                                        <input id="dropzone-file" name={"uploadedfile"} type="file" className="hidden" onChange={handleFile}/>
                                    </label>
                                </div>
                            )
                        }

                        {
                            isTraitement && (
                                <div className={"flex flex-col items-center"}>
                                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg"
                                         fill="none" viewBox="0 0 24 24">
                                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                                strokeWidth="4"></circle>
                                        <path className="opacity-75" fill="currentColor"
                                              d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"></path>
                                    </svg>
                                    <p className={"text-white"}>Traitement en cours...</p>
                                </div>
                            )
                        }

                        {
                            isLoading && (
                                <div className={"flex flex-col items-center"}>
                                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg"
                                            fill="none" viewBox="0 0 24 24">
                                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor"
                                                strokeWidth="4"></circle>
                                        <path className="opacity-75" fill="currentColor"
                                                d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"></path>
                                    </svg>
                                    <p className={"text-white"}>Loading...</p>
                                </div>
                            )
                        }

                        {
                            error && (
                                <div className={"flex items-center"}>
                                    <div className="flex items-center py-2 px-4 gap-2 bg-red-500 rounded-lg">
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-8 h-8">
                                            <path fillRule="evenodd" d="M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z" clipRule="evenodd" />
                                        </svg>
                                        <span className="font-medium">{error}</span>
                                    </div>
                                    <div className="flex items-center p-2 ml-2 bg-white rounded-full cursor-pointer" onClick={reset}>
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6">
                                            <path fillRule="evenodd" d="M4.755 10.059a7.5 7.5 0 0112.548-3.364l1.903 1.903h-3.183a.75.75 0 100 1.5h4.992a.75.75 0 00.75-.75V4.356a.75.75 0 00-1.5 0v3.18l-1.9-1.9A9 9 0 003.306 9.67a.75.75 0 101.45.388zm15.408 3.352a.75.75 0 00-.919.53 7.5 7.5 0 01-12.548 3.364l-1.902-1.903h3.183a.75.75 0 000-1.5H2.984a.75.75 0 00-.75.75v4.992a.75.75 0 001.5 0v-3.18l1.9 1.9a9 9 0 0015.059-4.035.75.75 0 00-.53-.918z" clipRule="evenodd" />
                                        </svg>
                                    </div>

                                </div>
                            )
                        }

                        {isFile && !isLoading && !error && (
                            <div className={"flex flex-col items-center"}>
                                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-40 h-40 text-blue-500">
                                    <path strokeLinecap="round" strokeLinejoin="round" d="M10.125 2.25h-4.5c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125v-9M10.125 2.25h.375a9 9 0 019 9v.375M10.125 2.25A3.375 3.375 0 0113.5 5.625v1.5c0 .621.504 1.125 1.125 1.125h1.5a3.375 3.375 0 013.375 3.375M9 15l2.25 2.25L15 12" />
                                </svg>

                                <form action="">
                                    <div className="flex flex-col">
                                        <label htmlFor="nbRays" className="block text-sm font-medium text-gray-200 mt-2">
                                            Number of rays :
                                        </label>
                                        <input type="number" name="nbRayon" id="nbRays" min={1} step={1} className="py-2 px-4 rounded-lg outline-blue-500" value={nbRays} onChange={(e) => {setNbRays(e.target.value)}}/>

                                        <label htmlFor="ndThreads" className="block text-sm font-medium text-gray-200 mt-4">
                                            Number of threads :
                                        </label>
                                        <input type="number" name="nbThreads" id="nbThreads" min={1} step={1} className="py-2 px-4 rounded-lg outline-blue-500" value={nbThreads} onChange={(e) => {setNbThreads(e.target.value)}}/>
                                    </div>
                                </form>

                                <button  className="px-4 py-2 mt-4 text-sm font-medium text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75" onClick={handleUpload}>
                                    Upload
                                </button>
                            </div>
                        )}
                    </form>

                    {
                        !isTraitement && result && (
                            <div className={"flex flex-col items-center"}>
                                <h1 className={"text-2xl font-bold text-white"}>Result :</h1>
                                <img src={result} alt="result" className={"w-1/2"}/>
                            </div>
                        )
                    }

                </div>
            </div>
        </div>
    )
}