import {GetMatches, DeleteUser, AddUser, UpdateUser, GetMatch} from "./utils/rest-calls";
import React from  'react';
import MatchTable from "./MatchTable";
import './MatchApp.css'
import MatchForm from "./MatchForm";

class MatchApp extends React.Component{
    constructor(props){
        super(props);
        this.state={users:[],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
            updateFunc:this.updateFunc.bind(this),
            // getOneFunc:this.getOneFunc.bind(this)
        }
        console.log('MatchApp constructor')
    }

    addFunc(user){
        console.log('inside add Func '+user);
        AddUser(user)
            .then(res=> GetMatches())
            .then(users=>this.setState({users}))
            .catch(erorr=>console.log('eroare add ',erorr));
    }


    deleteFunc(user){
        console.log('inside deleteFunc '+user);
        DeleteUser(user)
            .then(res=> GetMatches())
            .then(users=>this.setState({users}))
            .catch(error=>console.log('eroare delete', error));
    }

    updateFunc(user){
        console.log('inside updateFunc '+user);
        UpdateUser(user)
            .then(res=> GetMatches())
            .then(users=>this.setState({users}))
            .catch(error=>console.log('eroare update', error));
    }

    // getOneFunc(user){
    //     console.log('inside getOne '+user);
    //     GetMatch(user)
    //         .then(res => this.match = res)
    //         .then(v => console.log(this.match))
    //         .catch(error=>console.log('eroare getOne', error));
    //
    // }

    componentDidMount(){
        console.log('inside componentDidMount')
        GetMatches().then(users=>this.setState({users}));
    }

    render(){
        return(
            <div className="MatchApp">
                <h1>Match Management</h1>
                <MatchForm addFunc={this.state.addFunc} updateFunc={this.state.updateFunc} />
                <br/>
                <br/>
                <MatchTable users={this.state.users} deleteFunc={this.state.deleteFunc} />
            </div>
        );
    }
}

export default MatchApp;